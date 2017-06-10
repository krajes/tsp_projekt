package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Node {

    /** ------------------------------ */

    private static Integer nodeCounter = 0;

    /** ------------------------------ */

    private Integer id;
    private Integer cost;
    private HashMap<Integer, Link> linkMap;
    private Graph parent;

    private Coordinates2D c2d = null;

    /** ------------------------------ */

    public Node(Integer cost, Graph parent) {
        this.cost = cost;
        this.id = Node.nodeCounter++;
        this.parent = parent;
        this.linkMap = new HashMap<>();
    }

    public Node(Integer cost, Graph parent, Integer x, Integer y) {
        this(cost, parent);
        this.c2d = new Coordinates2D(x, y);
    }

    /** ------------------------------ */

    public Integer getCost() {
        return this.cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    /** ------------------------------ */

    public boolean linkWith(Integer destination, Integer cost, Boolean bidirectional) {

        Node destinationNode = this.getNodeFromParent(destination);

        if(destinationNode == null)
            return false;

        if(this.linkMap.containsKey(destination))
            return false;

        Link link = new Link(this, destinationNode, cost);
        this.addLink(destination, link);

        if(bidirectional) {
            Link loopback = new Link(destinationNode, this, cost);
            destinationNode.addLink(this.getId(), loopback);
        }

        return true;
    }

    public boolean removeLink(Integer destination) {
        return false;
    }

    public ArrayList<Link> getLinks() {

        Set<Integer> keySet = this.linkMap.keySet();

        ArrayList<Link> linkArray = new ArrayList<>();
        for(Integer key : keySet) linkArray.add(this.linkMap.get(key));

        return linkArray;
    }

    /** ------------------------------ */

    private boolean enlistedNode(Integer nodeId) {
        return this.linkMap.containsKey(nodeId);
    }

    private Node getNodeFromParent(Integer nodeId) {
        return parent.getNode(nodeId);
    }

    private void addLink(Integer destinationId, Link link) {
        this.linkMap.put(destinationId, link);
    }

    public Coordinates2D getCoordinates() {
        return this.c2d;
    }

    /** ------------------------------ */

    public Integer getId() {
        return this.id;
    }

    /** ------------------------------- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

