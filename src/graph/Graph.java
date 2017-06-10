package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Graph {

    /** ------------------------------ */

    private static Integer graphCounter = 0;

    private Integer id;
    private HashMap<Integer, Node> nodes;

    /** ------------------------------ */

    public Graph() {
        this.id = Graph.graphCounter++;
        this.nodes = new HashMap<>();

        //Debug.log("Graph created: (%d)", this.id);
    }

    /** ------------------------------ */

    public void addNode(Integer cost) {
        Node node = new Node(cost, this);
        this.nodes.put(node.getId(), node);

        //Debug.log("New node added: (%d)", node.getId());
    }

    public void addNode(Integer x, Integer y) {
        Node node = new Node(0, this, x, y);
        this.nodes.put(node.getId(), node);

        //Debug.log("New node added: (%d) [%d/%d]", node.getId(), x, y);
    }

    public boolean connectNodes(Integer sourceId, Integer destinationId, Integer cost, boolean bidir) {

        Node source = this.getNode(sourceId);

        if(source == null) {
            return false;
        }

        return source.linkWith(destinationId, cost, bidir);
    }

    public ArrayList<Node> getNodeList() {
        ArrayList<Node> nodes = new ArrayList<>();
        Set<Integer> keys = this.nodes.keySet();
        for(Integer key : keys)
            nodes.add(this.nodes.get(key));
        return nodes;
    }

    /** ------------------------------ */

    public boolean hasNode(Integer nodeId) {
        return this.nodes.containsKey(nodeId);
    }

    public Node getNode(Integer nodeId) {
        if(this.hasNode(nodeId) == false)
            return null;
        return this.nodes.get(nodeId);
    }

    /** ------------------------------ */

    public void printConnections() {
        Set<Integer> keys = this.nodes.keySet();
        for(Integer key : keys) {
            Node node = this.nodes.get(key);
            ArrayList<Link> linkList = node.getLinks();

            System.out.print(node.getId() + ": ");
            for(Link link : linkList)
                System.out.printf("%d->%d ", link.getDestination().getId(), link.getTravelCost());
            System.out.println("");
        }
    }
}
