package server;

import graph.Graph;
import algorithm.Naive;
import graph.Node;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import graph.Coordinates2D;

public class Display extends JFrame implements ActionListener {

    DrawablePanel pDisplay;
    JPanel pNavigation;
    JButton bNewElement, bNaiveSearch;
    Graph graph;

    Naive naive;

    public Display() {
        super("Visualizer");
        this.setLayout(new BorderLayout());

        this.add(this.setupDisplay(), BorderLayout.CENTER);
        this.add(this.setupNavigation(), BorderLayout.SOUTH);

        this.setPreferredSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                pDisplay.addElementEx(mouseEvent.getX(), mouseEvent.getY(), 0);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    private JPanel setupDisplay() {
        this.pDisplay = new DrawablePanel();
        return this.pDisplay;
    }

    private JPanel setupNavigation() {
        this.pNavigation = new JPanel(new FlowLayout(FlowLayout.LEFT));

        /*this.bNewElement = new JButton("New element");
        this.bNewElement.addActionListener(this);
        this.pNavigation.add(this.bNewElement);*/

        this.bNaiveSearch = new JButton("Start naive search");
        this.bNaiveSearch.addActionListener(this);
        this.pNavigation.add(this.bNaiveSearch);

        return this.pNavigation;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object caller = actionEvent.getSource();
        if(caller == this.bNewElement) {
            System.out.println("Called");
            this.pDisplay.addElement();
        } else if(caller == this.bNaiveSearch) {
            System.out.println("Wait..");
            Pair<ArrayList<Integer>, Integer> result = this.naive.generateSequence(0);
            this.pDisplay.addSequence(result.getKey(), 0, result.getValue());
            this.pDisplay.forceRepaint();
            System.out.println("Done");

        }
    }

    public void attachGraph(Graph g) {
        this.graph = g;
        this.pDisplay.addData(g.getNodeList());
        this.naive = new Naive(g);
    }
}

class DrawablePanel extends JPanel {
    /** Our entities a.k.a vertices */
    private ArrayList<Entity> entityList = new ArrayList<>();
    /** List of the best sequence generated by given algorithm (Display->actionPerformed->caller:bNaiveSearch) */
    private ArrayList<Integer> sequence = new ArrayList<>();

    /** Display info: how wide's the entity? */
    private static final Integer entitySize = 8;
    private static final Integer entityMPMod = DrawablePanel.entitySize / 2;
    private static final Random random = new Random();
    /** Where's the information box located */
    private static Coordinates2D entityInfoBox = new Coordinates2D(20, 20);
    /** Some data from route-calculation algorithm */
    private Integer root = 0;
    private Integer route = 0;
    private Integer bestRoute = Integer.MAX_VALUE;

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        this.setBackground(Color.BLACK);
        this.drawEntityInfo(graphics);

        this.drawSequence();

        for(Entity entity : this.entityList) {
            this.drawEntity(entity, graphics);
        }
    }

    /**
     * This method forces repaint of the panel.
     */
    public void forceRepaint() {
        this.paint(this.getGraphics());
    }

    /**
     * This method adds nodes to the memory.
     * @param nodes
     */
    public void addData(ArrayList<Node> nodes) {
        for(Node node : nodes) {
            this.addElementEx(node.getCoordinates().getX(), node.getCoordinates().getY(), node.getId());
        }
    }

    public void addElementEx(Integer x, Integer y, Integer eId) {

        Coordinates2D coords = new Coordinates2D(x, y);
        entityList.add(new Entity(coords, 0, eId));
        this.paintAll(this.getGraphics());
    }

    public void addElement() {

        Coordinates2D coords = new Coordinates2D(
                (int)(random.nextFloat() * this.getSize().getWidth()),
                (int)(random.nextFloat() * this.getSize().getHeight())
        );
        entityList.add(new Entity(coords, 0, 0));
        this.paintAll(this.getGraphics());
    }

    public void addSequence(ArrayList<Integer> sequence, Integer root, Integer route) {
        if(this.bestRoute >= route) {
            this.root = root;
            this.route = route;
            this.sequence = sequence;
            this.bestRoute = this.route;
        }
    }

    private void drawEntityInfo(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawString("--- Graph info ---", DrawablePanel.entityInfoBox.getX(), DrawablePanel.entityInfoBox.getY());
        g.drawString(String.format("Vertices: %d", this.entityList.size()), DrawablePanel.entityInfoBox.getX() + 5, DrawablePanel.entityInfoBox.getY() + 20);
        g.drawString(String.format("Minimum distance: %d", this.route), DrawablePanel.entityInfoBox.getX() + 5, DrawablePanel.entityInfoBox.getY() + 38);
        g.drawString(String.format("Root: %d", this.root), DrawablePanel.entityInfoBox.getX() + 5, DrawablePanel.entityInfoBox.getY() + 52);
        g.drawString(String.format("Best cycle: %s", this.getBestCycleInfo()), DrawablePanel.entityInfoBox.getX() + 5, DrawablePanel.entityInfoBox.getY() + 66);
    }

    private String getBestCycleInfo() {
        StringBuilder sb = new StringBuilder();
        for(Integer elementId : this.sequence) {
            sb.append(String.format("%d ", elementId));
        }
        return sb.toString();
    }

    private void drawEntity(Entity e, Graphics g) {
        if(e.isAcitve())
            g.setColor(Color.RED);
        else
            g.setColor(Color.GREEN);

        int x = (int)(e.getCoordinates().getX() - (entityMPMod)),
                y = (int)(e.getCoordinates().getY() - (entityMPMod));

        g.fillOval(x, y, DrawablePanel.entitySize, DrawablePanel.entitySize);

        g.setColor(Color.WHITE);
        g.drawString(e.getEntityName(), x + 8, y - 3);
    }

    private void drawSequence() {
        if(this.sequence.size() > 1)
            for(int index = 1; index < this.sequence.size(); index++)
                this.drawSnapLine(
                        this.entityList.get(this.sequence.get(index-1)),
                        this.entityList.get(this.sequence.get(index)),
                        this.getGraphics(), Color.GREEN);
    }


    private void drawSnapLine(Entity from, Entity to, Graphics g, Color color) {
        g.setColor(color);
        g.drawLine(from.getCoordinates().getX() , from.getCoordinates().getY(),
                to.getCoordinates().getX(), to.getCoordinates().getY());

        /*Integer distance = from.getCoordinates().distance(to.getCoordinates());
        Coordinates2D mid = from.getCoordinates().midTo(to.getCoordinates());
        g.drawString(String.format("%d", distance), mid.getX(), mid.getY() - 2);*/
    }
}

class Entity {
    private Coordinates2D coordinates;
    private Integer saveZoneRadius;
    private String entityName = "Entity";
    private Boolean activeEntity = false;
    private Integer entityId = 0;

    public Entity(Coordinates2D coordinates, Integer saveZoneRadius, Integer entityId) {
        this.coordinates = coordinates;
        this.saveZoneRadius = saveZoneRadius;
        this.entityName = String.format("(%d) Entity [%d/%d]", entityId, this.coordinates.getX(), this.coordinates.getY());
        this.entityId = entityId;
    }

    public Coordinates2D getCoordinates() {
        return this.coordinates;
    }

    public Integer getSaveZoneRadius() {
        return this.saveZoneRadius;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public Boolean isAcitve() {
        return this.activeEntity;
    }
}