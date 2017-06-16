package client.guiassets;

import javax.swing.*;
import java.io.File;

public class FSavePanel extends AbstractSelectionPanel {

    public FSavePanel(String name) {
        super(name);
    }

    @Override
    void onFilePathButtonClick() {
        System.out.println("OpenSaveDialog..");
        this.OpenSaveDialog();
    }

    private final JFileChooser fileChooser = new JFileChooser();

    public void OpenSaveDialog() {
        int fcRes = fileChooser.showSaveDialog(this);
        if (fcRes == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.pathToFile = file.getAbsolutePath();
            this.updateFilePathDisplay();
        } else {}
    }
}
