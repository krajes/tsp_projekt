package client.guiassets;

import graph.Graph;
import graph.Node;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class GraphDataPanel extends JPanel{

    private JPanel rootSelectionPanel;
    private JComboBox<String> nodeList;
    private ArrayList<Integer> nodeData = null;
    private final Border border = new EmptyBorder(new Insets(10,10,10,10));

    public GraphDataPanel() {
        this.setLayout(new GridLayout(2,1));
        this.setupRootSelection();

        this.add(this.rootSelectionPanel);
        this.setVisible(true);
    }

    private void setupRootSelection() {
        this.rootSelectionPanel = new JPanel(new GridLayout(2,1));
        this.rootSelectionPanel.add(new JLabel("Wybierz wierzchołek startowy (root)"));

        this.nodeList = new JComboBox<>();
        this.rootSelectionPanel.add(this.nodeList);

        this.rootSelectionPanel.setBorder(border);
    }

    public void setData(ArrayList<Integer> data) {
        this.nodeData = data;
    }

    public void update() {
        if(this.nodeData == null) return;

        this.nodeList.removeAllItems();
        for(Integer nodeId : this.nodeData)
            this.nodeList.addItem(String.format("%d", nodeId));
    }

    public Integer getSelectedId() {
        return Integer.parseInt(this.nodeList.getItemAt(this.nodeList.getSelectedIndex()));
    }
}
