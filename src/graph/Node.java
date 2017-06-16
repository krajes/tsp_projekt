package graph;

import exceptions.DuplicatedNodeException;
import exceptions.NodeConnectedException;
import exceptions.NodeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Node {

    /** ------------------------------ */

    private Integer id;
    private Integer cost;
    private HashMap<Integer, Link> linkMap;
    private Graph parent;

    private Coordinates2D c2d = null;

    /** ------------------------------ */

    public Node(Integer nodeId, Integer cost, Graph parent) throws DuplicatedNodeException {

        this.cost = cost;

        if (nodeId != -1) {
            if (parent.hasNode(nodeId)) throw new DuplicatedNodeException();
            this.id = nodeId;
        }

        this.parent = parent;
        this.linkMap = new HashMap<>();

        // System.out.printf("[Node]: Created node with id %d\n", this.id);
    }

    public Node(Integer nodeId, Integer cost, Graph parent, Integer x, Integer y) throws DuplicatedNodeException {
        this(nodeId, cost, parent);
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

    public boolean linkWith(Integer destination, Integer cost, Boolean bidirectional) throws NodeConnectedException {

        Node destinationNode = this.getNodeFromParent(destination);

        if(destinationNode == null)
            return false;

        // Polaczenie z tego wierzcholka do destination juz istnieje
        if(this.linkMap.containsKey(destination)) {
            //return false;
            throw new NodeConnectedException();
        }

        Link link = new Link(this, destinationNode, cost);
        this.addLink(destination, link);

        if(bidirectional) {
            Link loopback = new Link(destinationNode, this, cost);
            destinationNode.addLink(this.getId(), loopback);
        }

        return true;
    }

    public Link getLink(Integer destination) {
        if(this.linkMap.containsKey(destination))
            return this.linkMap.get(destination);
        return null;
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

