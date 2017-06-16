package client.guiassets;

import exceptions.AbstractGUIException;
import exceptions.GUIElementNotInitializedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractSelectionPanel extends JPanel {

    /** ----------------------------------------------------------------------------------- Zmienne ----------------- */

    private JPanel nameContainer, uiContainer;
    private JLabel panelName;
    private JButton fileSelectButton;
    private JTextField filePath;
    protected String pathToFile = "...";

    /** ------------------------------------------------------------------------------- Konstruktor ----------------- */

    public AbstractSelectionPanel(String name) {
        try {
            this.setupNameContainer(name);
            this.setupUIContainer();
            this.setupMainContainer();
        } catch(AbstractGUIException ex) {
            ex.printStackTrace();
        }
        this.setVisible(true);
    }

    /** ------------------------------------------------------------------------------- Setupy ---------------------- */

    private void setupMainContainer() throws AbstractGUIException {
        if(nameContainer == null && uiContainer == null) throw new GUIElementNotInitializedException();
        this.setLayout(new GridLayout(2,1));
        this.add(this.nameContainer);
        this.add(this.uiContainer);
        this.setVisible(true);
    }

    private void setupNameContainer(String panelName) {
        this.nameContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.panelName = new JLabel(panelName);
        this.nameContainer.add(this.panelName);
        this.nameContainer.setVisible(true);
    }

    private void setupUIContainer() {
        this.uiContainer = new JPanel(new BorderLayout());
        this.fileSelectButton = new JButton("...");
        this.filePath = new JTextField("");
        this.filePath.setEditable(false);

        this.uiContainer.add(this.fileSelectButton, BorderLayout.WEST);
        this.uiContainer.add(this.filePath, BorderLayout.CENTER);
        this.uiContainer.setVisible(true);

        this.fileSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button clicked..");
                onFilePathButtonClick();
            }
        });
    }

    /** ------------------------------------------------------------------------------- Gettery --------------------- */

    public String getPath() {
        return this.pathToFile;
    }

    abstract void onFilePathButtonClick();

    void updateFilePathDisplay() {
        this.filePath.setText(this.pathToFile);
    }
}
