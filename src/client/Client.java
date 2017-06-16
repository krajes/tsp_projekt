package client;

import bridge.DataToServer;
import bridge.NodeConnectionInfo;
import bridge.OutputData;
import client.guiassets.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client extends JFrame {

    // ------------------------------------------------------------------------------ Zmienne --------------------------
    private JTabbedPane mainDisplay;

    private AbstractSelectionPanel inputFileSelector, outputFileSelector;
    private JPanel ioPanel;

    private GraphDataPanel graphDataPanel;

    private OutputFormatPanel outputFormatPanel;

    private JPanel optionsPanel;
    private JButton opRead, opPush;

    private OutputData outputData = null;

    ArrayList<Integer> nodesFromFile;

    private final String defaultFormatting = "Minimalny koszt: ${MIN_COST}, Ilość porównań: ${COMP_COUNT}, " +
            "Pierwszy koszt: ${FIRST_COST}, Cykl: ${CYCLE}";

    // -------------------------------------------------------------------------- Konstruktor --------------------------

    public Client() {
        super("TSP");
        this.setLayout(new BorderLayout());

        /** --------------------------------------------------------------------- Glowny panel ---------------------- */
        this.inputFileSelector  = new FLoadPanel("Wybierz plik z danymi wejsciowymi");
        this.outputFileSelector = new FSavePanel("Wybierz ścieżkę wyjściową");
        this.ioPanel = new JPanel(new GridLayout(2,1));
        this.ioPanel.add(this.inputFileSelector);
        this.ioPanel.add(this.outputFileSelector);

        this.outputFormatPanel = new OutputFormatPanel();
        this.graphDataPanel = new GraphDataPanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        this.setupOptionsPanel();
        mainPanel.add(this.ioPanel, BorderLayout.NORTH);
        mainPanel.add(this.graphDataPanel, BorderLayout.CENTER);
        mainPanel.add(this.optionsPanel, BorderLayout.SOUTH);

        this.mainDisplay = new JTabbedPane();
        this.mainDisplay.addTab("Główne", mainPanel);
        this.mainDisplay.addTab("Formatowanie wyjsciowe", this.outputFormatPanel);

        this.add(mainDisplay, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        
    }

    // ------------------------------------------------------------------------ Konfiguratory --------------------------

    private void setupOptionsPanel() {
        this.optionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        this.opRead = new JButton("Wczytaj plik");
        this.opPush = new JButton("Wyślij do serwera");

        this.optionsPanel.add(this.opRead);
        this.optionsPanel.add(this.opPush);

        this.opRead.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readAndParseInputFile();
            }
        });

        this.opPush.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendDataToServer();
            }
        });
    }

    // ---------------------------------------------------------------------- ActionListenery --------------------------

    private void readAndParseInputFile() {
        InputProcessor.parseFile(this.inputFileSelector.getPath(), this);
        this.nodesFromFile = InputProcessor.getNodeList();
        this.graphDataPanel.setData(this.nodesFromFile);
        this.graphDataPanel.update();
    }

    private void sendDataToServer() {
        try {
            // Przygotuj dane
            DataToServer dts = new DataToServer(this.graphDataPanel.getSelectedId(), InputProcessor.getNodeList(), InputProcessor.getNodeConnectionInfo());

            // Otwieramy socket
            Socket clientSocket = null;
            clientSocket = new Socket(InetAddress.getLocalHost(), 444);

            // Strumienie
            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

            // Wysyłamy pakiet
            outToServer.writeObject(dts);

            // Pobieramy wynik
            this.outputData = (OutputData) inFromServer.readObject();

            // Zamykamy socket
            clientSocket.close();
            JOptionPane.showMessageDialog(this, "Transakcja została zakończona.");

            this.toFile(this.outputData);

        } catch(NullPointerException npe) {
            JOptionPane.showMessageDialog(this, "Brak danych wejsciowych..");
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "Nieznany host podczas próby pobrania adresu.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Błąd I/O.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Wykryto próbę zapisu do klasy nieserialozowanej.");
            e.printStackTrace();
        }
    }

    private void toFile(OutputData data) {
        // Sprawdzamy, czy nasz klient posiada w pamieci dane wynikowe
        if(data == null) {
            JOptionPane.showMessageDialog(this, "Dane wynikowe nie zostały jeszcze umieszczone w pamieci");
            return;
        }

        // Sprawdzenie sciezki zapisu
        String filePath = this.outputFileSelector.getPath();
        if(filePath.length() <= 5) {
            JOptionPane.showMessageDialog(this, "Nie można zapisać wyniku w wybranej lokacji!");
            return;
        }

        // Zapisujemy plik na dysk
        try{
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            OutputDataFormatter odf = new OutputDataFormatter(this.outputData);

            String userPattern = this.outputFormatPanel.getPattern();
            writer.write(odf.getParsedString(userPattern.length() < 2 ? defaultFormatting : userPattern));

            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Wystąpił błąd IO podczas zapisu.");
        }

    }

    // ------------------------------------------------------------------------- Statyczne -----------------------------

    public static void main(String[] args) {
        Client c = new Client();
    }
}
