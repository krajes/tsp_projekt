package client.guiassets;

import bridge.OutputData;
import client.OutputDataFormatter;
import exceptions.AbstractGUIException;
import exceptions.GUIElementNotInitializedException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OutputFormatPanel extends JPanel {

    /** -------------------------------------------------------------- Zmienne -------------- */

    private JPanel hintPanel, textPanel, tagPanel;
    private JTextArea userInput, processedOutput;
    private OutputData sampleOutput;
    private OutputDataFormatter odf;

    private String format;

    /** -------------------------------------------------------------- Konstruktor -------------- */

    public OutputFormatPanel() {
        try {
            this.setupHintPanel();
            this.setupTextPanel();
            this.setupTagPanel();
            this.setupMain();
        } catch (AbstractGUIException age) {
            age.printStackTrace();
        }

        this.sampleOutput = new OutputData();
        this.sampleOutput.generateRandom();
        this.odf = new OutputDataFormatter(this.sampleOutput);
    }

    /** -------------------------------------------------------------- Setupy -------------- */

    private void setupMain() throws AbstractGUIException {
        if(hintPanel == null || textPanel == null || tagPanel == null)
            throw new GUIElementNotInitializedException();
        this.setLayout(new BorderLayout());
        this.add(this.hintPanel, BorderLayout.NORTH);
        this.add(this.textPanel, BorderLayout.CENTER);
        this.add(this.tagPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void setupHintPanel() {
        this.hintPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextArea textArea =
                new JTextArea(
                        "Tutaj możesz wprowadzić formatowanie pliku tekstowego. Po prawej stronie zostanie wyświetlony sformatowany (przykładowy) wynik",
                        0, 42);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        this.hintPanel.add(textArea);
        this.hintPanel.setVisible(true);
    }

    private void setupTextPanel() {
        this.textPanel = new JPanel(new GridLayout(1,2));
        Border eb =  BorderFactory.createLineBorder(Color.GRAY, 1);
        Dimension dim = new Dimension(180, 260);

        this.userInput = new JTextArea();
        this.userInput.setPreferredSize(dim);
        this.userInput.setBorder(eb);
        this.textPanel.add(this.userInput);
        this.userInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                onKeyReleased();
            }
        });

        this.processedOutput = new JTextArea();
        this.processedOutput.setPreferredSize(dim);
        this.processedOutput.setBorder(eb);
        this.textPanel.add(this.processedOutput);

        this.processedOutput.setEditable(false);
    }

    private void setupTagPanel() {
        this.tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextArea textArea =
                new JTextArea(
                        "Dostępne tagi (formatowanie: ${NAZWA_TAGU}): MIN_COST - najmniejszy obliczony koszt, " +
                                "COMP_COUNT - ilość wykonanych porównań, " +
                                "FIRST_COST - wartość pierwszego obliczonego kosztu, " +
                                "CYCLE - wypisuje cykl w postaci ciągu numerów wierzchołków.",
                        0, 42);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        this.tagPanel.add(textArea);
        this.tagPanel.setVisible(true);
    }

    private void onKeyReleased() {
        this.format = this.userInput.getText();
        this.processedOutput.setText(this.odf.getParsedString(this.format));
    }

    public String getPattern() {
        return this.userInput.getText();
    }

}
