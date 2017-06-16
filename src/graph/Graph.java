package graph;

import exceptions.DuplicatedNodeException;
import exceptions.NodeConnectedException;
import exceptions.NodeException;

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

    public Set<Integer> getNodeIds() {
        return nodes.keySet();
    }

    /** ------------------------------ */

    public Node addNode(Integer nodeId, Integer cost) throws DuplicatedNodeException {
        Node node = new Node(nodeId, cost, this);
        this.nodes.put(node.getId(), node);
        //Debug.log("New node added: (%d)", node.getId());
        return node;
    }

    public Node addNode(Integer nodeId, Integer cost, Integer x, Integer y) throws DuplicatedNodeException {
        Node node = new Node(nodeId, cost, this, x, y);
        this.nodes.put(node.getId(), node);

        //Debug.log("New node added: (%d) [%d/%d]", node.getId(), x, y);
        return node;
    }

    public boolean connectNodes(Integer sourceId, Integer destinationId, Integer cost, boolean bidir) {

        // Get our source
        Node source = this.getNode(sourceId);

        // Check if there is such node (not null)
        if(source == null) {
            //Debug.log("(conenctNodes): Source node->null");
            return false;
        }

        // Try to connect
        Boolean result = false;
        try {
            result = source.linkWith(destinationId, cost, bidir);
        } catch( NodeConnectedException nce ) {
            // TODO: obsluga
            System.out.println(String.format("Połączenie %d->%d już istnieje.", sourceId, destinationId));
            //nce.printStackTrace();
        }
        return result;
    }

    public ArrayList<Node> getNodeList() {
        ArrayList<Node> nodes = new ArrayList<>();
        Set<Integer> keys = this.nodes.keySet();
        for(Integer key : keys)
            nodes.add(this.nodes.get(key));
        return nodes;
    }

    /** ------------------------------ */

    public boolean coherencyTest() {
        Set<Integer> nodeIdSet = this.getNodeIds();

        if (nodeIdSet.isEmpty()) {
            return false;
        }

        Integer expectedValue = nodeIdSet.size() - 1;
        //System.out.println("Expected value: " + expectedValue);
        for(Integer key : nodeIdSet) {
            //System.out.println(String.format("For key %d -> %d", key, this.nodes.get(key).getLinks().size()));
            if (this.nodes.get(key).getLinks().size() != expectedValue)
                return false;
        }
        return true;
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
