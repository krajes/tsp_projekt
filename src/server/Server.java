/**
 * Do zrobienia pozostalo:
 * Klient/serwer
 * Wczytywanie danych wejsciowych
 * Formatowanie danych wyjsciowych
 *
 * Algorytm dziala na zasadzie naiwnej eliminacji (poki co na sztywno start w wierzcholku 0).
 * Generowane sa rozne cykle, zliczane sa ich koszty. Po kazdej iteracji sprawdzamy, czy koszt cyklu
 * jest mniejszy lub rowny od najlepszego (tzn. najmniejszego). Jesli tak, to ustawiamy cykl jako najlepszy (najkrotszy).
 * Wykonujemy ta petle pewna ilosc razy (poki co ustawione na sztywno: 1000). Dzieki temu uzyskujemy calkiem dobry wynik
 * w dosc krotkim czasie.
 */
package server;

import algorithm.Naive;
import bridge.DataToServer;
import bridge.NodeConnectionInfo;
import bridge.OutputData;
import exceptions.DuplicatedNodeException;
import graph.Graph;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

enum MessageTypes {
    MESSAGE_INFO,
    MESSAGE_DEBUG,
    MESSAGE_SERVER,
}

public class Server extends JFrame implements ActionListener {

    // -------------------------------------------------------------------------------------- Zmienne ------------------

    private boolean isOn = true;

    private JPanel options, log, status, serverStatusPanel;
    private JScrollPane logScrollPane;
    private JTextPane logPlaceholder;
    private JButton toggle, clearLog;
    private JLabel serverStatus;
    private Font defaultButtonFont, inactiveButtonFont;

    // LOG DEBUG!!
    private JPanel debugPanel;
    private JButton confirm;
    private JTextField debugData;

    ServerSocket socketListener;
    private final Integer port = 444;

    private Graph graph;

    // ---------------------------------------------------------------------------------- Konstrutkor ------------------


    public Server() {
        // Setup okienka
        super("Setting up");
        this.setLayout(new BorderLayout());

        // Setup elementów
        this.setupFonts();
        this.setupPanels();
        //this.add(this.options, BorderLayout.NORTH);
        this.add(this.log, BorderLayout.CENTER);
        this.add(this.status, BorderLayout.SOUTH);
        this.updateUI();

        // Pozostałe rzeczy
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.updateWindowName();
        this.addLog("Serwer jest gotowy do uzycia", MessageTypes.MESSAGE_INFO);

        this.listen();
    }

    private void listen() {
        this.addLog("Rozpoczyam nasłuchiwanie na porcie " + this.port, MessageTypes.MESSAGE_SERVER);
        try {
            this.socketListener = new ServerSocket(444);
            while (true) {
                // Oczekiwanie na klienta
                this.addLog("Oczekuję na kolejny pakiet od klienta..", MessageTypes.MESSAGE_SERVER);
                Socket clientSocket = socketListener.accept();
                this.addLog("Otrzymałem pakiet. Przetwarzanie danych..", MessageTypes.MESSAGE_SERVER);

                // Strumienie I/O (klient <-> serwer)
                ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());

                // Pobieramy paczkę
                DataToServer dts = (DataToServer) inFromClient.readObject();

                // Wyswietlamy informacje
                this.addLog("ID wierzchołka startowego: " + dts.getRootNodeId(), MessageTypes.MESSAGE_INFO);
                this.addLog("Ilość wierzchołków w grafie: " + dts.getNodeList().size(), MessageTypes.MESSAGE_INFO);

                // Generujemy wynik
                OutputData od = this.routine(dts);
                if(od == null)
                    this.addLog("Algorytm nie zwrócił wyniku. Kicha!", MessageTypes.MESSAGE_SERVER);
                else
                    this.addLog("Wysyłam klientowi wynik algorytmu.. ", MessageTypes.MESSAGE_SERVER);

                outToClient.writeObject(od);
                this.addLog("Transakcja zakończona, boii! :)", MessageTypes.MESSAGE_SERVER);
            }
        } catch (IOException e) {
            this.addLog("Wystąpił błąd IO!", MessageTypes.MESSAGE_SERVER);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            this.addLog("Próba przetworzenia nieserializowanej klasy!", MessageTypes.MESSAGE_SERVER);
            e.printStackTrace();
        }
    }

    private OutputData routine(DataToServer dts) {

        try {
            // Generujemy graf
            this.addLog("Generuję graf..", MessageTypes.MESSAGE_INFO);
            graph = new Graph();
            System.out.println("Graph: " + graph);

            // Dodajemy wierzchołki
            ArrayList<Integer> nodeList = dts.getNodeList();
            for (Integer node : nodeList)
                graph.addNode(node, 0);
            this.addLog(String.format("Wczytano %d wierzchołków", graph.getNodeList().size()), MessageTypes.MESSAGE_INFO);

            // Laczymy wierzcholki zgodnie z danymi pobranymi od klienta
            Integer connections = 0;
            ArrayList<NodeConnectionInfo> nci = dts.getNodeConnectionInfos();
            for(NodeConnectionInfo nodeConnectionInfo : nci) {
                ArrayList<Pair<Integer, Integer>> indicyList = nodeConnectionInfo.getDestinationList();
                Integer source = nodeConnectionInfo.getSource();
                for(Pair<Integer, Integer> singleLink : indicyList) {
                    graph.connectNodes(source, singleLink.getKey(), singleLink.getValue(), true);
                    connections++;
                }
            }
            this.addLog(String.format("Wczytano %d krawędzi", graph.getNodeList().size()), MessageTypes.MESSAGE_INFO);
            graph.printConnections();

            // Testujemy, czy graf jest grafem pelenym (relacja wierzchołków: każdy<->każdy)
            this.addLog("Testuję graf..", MessageTypes.MESSAGE_INFO);
            if(!graph.coherencyTest()) {
                this.addLog("Test zakończony niepowodzeniem. Przerywam działanie.", MessageTypes.MESSAGE_INFO);
                return null;
            }
            this.addLog("Test zakończony pomyślnie.", MessageTypes.MESSAGE_INFO);

            // Wykonujemy operacje na grafie
            this.addLog("Rozpoczynam generowanie marszruty", MessageTypes.MESSAGE_INFO);

            Naive naive = new Naive(graph);
            naive.generateSequence(dts.getRootNodeId());

            OutputData od = naive.getOutputData();
            this.addLog("Wykonano. Zwracam wynik do głównego procesu.", MessageTypes.MESSAGE_INFO);

            graph = null;
            return od;
        } catch (DuplicatedNodeException e) {
            this.addLog("Wykryto duplikację wierzchołka. Odrzucam.", MessageTypes.MESSAGE_INFO);
            e.printStackTrace();
        }
        return null;
    }

    // ----------------------------------------------------------------------------------- Konfiguratory ---------------

    private void setupPanels() {
        // Tworzenie paneli
        this.options = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.log = new JPanel(new GridLayout(0,1));
        this.status = new JPanel(new BorderLayout());

        // Dodawanie elementow do paneli

        // Menu
        this.toggle = new JButton("Włącz serwer");
        this.clearLog  = new JButton("Wyczyść log");

        // Log
        this.logPlaceholder = new JTextPane();
        this.logScrollPane = new JScrollPane(this.logPlaceholder);
        this.log.add(this.logScrollPane);
        this.logPlaceholder.setEditable(false);
        log.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        this.logPlaceholder.setPreferredSize(new Dimension(320, 160));

        // ActionListener
        this.toggle.addActionListener(this);
        this.clearLog.addActionListener(this);

        this.options.add(this.toggle);
        this.options.add(this.clearLog);

        // Status
        this.serverStatusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.serverStatus = new JLabel("Nieznany");
        this.serverStatus.setForeground(Color.DARK_GRAY);
        this.serverStatusPanel.add(new JLabel("Status serwera:"));
        this.serverStatusPanel.add(this.serverStatus);
        this.status.add(this.serverStatusPanel, BorderLayout.NORTH);
        this.serverStatusPanel.setBackground(Color.LIGHT_GRAY);

        // Debug panel
        this.debugPanel = new JPanel(new BorderLayout());
        this.confirm = new JButton("Wyślij");
        this.confirm.addActionListener(this);
        this.debugData = new JTextField();
        this.debugPanel.add(this.confirm, BorderLayout.WEST);
        this.debugPanel.add(this.debugData, BorderLayout.CENTER);

        //this.status.add(this.debugPanel, BorderLayout.SOUTH);

        // Style przycisków
        this.defaultButtonStyle(this.toggle, this.clearLog, this.confirm);
    }

    private void defaultButtonStyle(JButton... refs) {
        for(JButton ref : refs) {
            ref.setBackground(new Color(0xB6E86B));
            ref.setFont(defaultButtonFont);
        }
    }

    private void setupFonts() {
        defaultButtonFont = new Font("Arial", Font.PLAIN, 14);
        inactiveButtonFont = new Font("Arial", Font.ITALIC, 14);
    }

    // ------------------------------------------------------------------------------------- Updatery ------------------

    private void updateUI() {
        if(this.isOn) {
            this.serverStatus.setText("Włączony");
            this.serverStatus.setForeground(Color.GREEN);
            this.toggle.setText("Wyłącz serwer");
        } else {
            this.serverStatus.setText("Wyłączony");
            this.serverStatus.setForeground(Color.RED);
            this.toggle.setText("Włącz serwer");
        }
        this.updateWindowName();
    }

    private void updateWindowName() {
        this.setTitle(this.getWindowName());
    }

    private String getWindowName() {
        return "Server :: " + ((this.isOn) ? "On" : "Off");
    }


    private void addLog(String log, MessageTypes type) {
        Color tagColor; String tag;
        switch(type) {
            case MESSAGE_INFO:
                tagColor = Color.LIGHT_GRAY;
                tag = "[Info]: ";
                break;
            case MESSAGE_SERVER:
                tagColor = Color.BLUE;
                tag = "[Server]: ";
                break;
            case MESSAGE_DEBUG:
                tagColor = Color.GREEN;
                tag = "[Debug]: ";
                break;
            default:
                tag = "[Unknown]: ";
                tagColor = Color.BLACK;
        }

        this.logPlaceholder.setEditable(true);
        appendToPane(this.logPlaceholder, tag, tagColor);
        appendToPane(this.logPlaceholder, log + "\n", Color.BLACK);
        this.logPlaceholder.setEditable(false);
    }

    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    // ------------------------------------------------------------------------------------ ActionListenery ------------

    public void actionPerformed(ActionEvent actionEvent) {
        Object o = actionEvent.getSource();

        if(o.equals(this.clearLog))     this.onClearLogButtonClick();
        else if(o.equals(this.toggle))  this.onToggleButtonClick();
        else if(o.equals(this.confirm)) this.onDebugConfirmClick();
    }

    private void onDebugConfirmClick() {
        if(this.debugData.getText().length() > 0) {
            // Pobierz dane i dodaj do logu
            this.addLog(this.debugData.getText(), MessageTypes.MESSAGE_DEBUG);
            // Wyczysc linijke
            this.debugData.setText("");
        }
    }

    private void onClearLogButtonClick() {
        // Wyczysc log i usun zawartosc stringBuildera
        this.logPlaceholder.setText("");
    }

    private void onToggleButtonClick() {
        if(this.isOn) this.stopServer();
        else this.startServer();
    }

    private void stopServer() {
        serverToggleButtonUpdate("Zatrzymuje dzialanie serwera..", false);

        this.isOn = false;
        this.updateUI();
        serverToggleButtonUpdate("Serwer zostal zatrzymany", true);

    }

    private void startServer() {
        serverToggleButtonUpdate("Uruchamiam serwer..", false);

        this.isOn = true;
        this.updateUI();
        serverToggleButtonUpdate("Serwer oczekuje na polaczenia!", true);
    }

    private void serverToggleButtonUpdate(String message, Boolean toggleFlag) {

        Font font = null;
        if(toggleFlag)
            font = this.defaultButtonFont;
        else
            font = this.inactiveButtonFont;

        this.toggle.setEnabled(toggleFlag);
        this.toggle.setFont(font);
        this.addLog(message, MessageTypes.MESSAGE_SERVER);
    }

    // -------------------------------------------------------------- Metody statyczne ---------------------------------

    public static Random rg = new Random();
    public static void main(String[] args) {
        Server server = new Server();
        //debugDisplayTest();
    }

    public static void debugDisplayTest() {
        // Logic
        Graph g = new Graph();

        try {
            for (int i = 0; i < 10; i++)
                g.addNode(i, 0, 100 + rg.nextInt(900), 100 + rg.nextInt(600));
        } catch (DuplicatedNodeException dne) {
            dne.printStackTrace();
        }

        System.out.println("Coherency test: " + (g.coherencyTest() ? "Passed" : "Failed"));

        for(int inner = 0; inner < g.getNodeList().size(); inner++)
            for(int outter = 0; outter < g.getNodeList().size(); outter++)
                if(inner != outter)
                    g.connectNodes(inner, outter,
                            10, //g.getNode(inner).getCoordinates().distance(g.getNode(outter).getCoordinates()),
                            false);

        System.out.println("Coherency test: " + (g.coherencyTest() ? "Passed" : "Failed"));

        g.printConnections();
        // Wyswietlanie (tylko do debugu)
        Display display = new Display();
        display.attachGraph(g);
    }

}