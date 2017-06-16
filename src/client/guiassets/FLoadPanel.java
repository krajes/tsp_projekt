package client.guiassets;

import javax.swing.*;
import java.io.File;

public class FLoadPanel extends AbstractSelectionPanel {

    public FLoadPanel(String name) {
        super(name);
    }

    @Override
    void onFilePathButtonClick() {
        System.out.println("OpenLoadDialog..");
        this.OpenLoadDialog();
    }

    private final JFileChooser fileChooser = new JFileChooser();

    public void OpenLoadDialog() {
        int fcRes = fileChooser.showOpenDialog(this);
        if (fcRes == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.pathToFile = file.getAbsolutePath();
            this.updateFilePathDisplay();
        } else {}
    }
}
