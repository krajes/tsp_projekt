package graph;

import graph.Node;

public class Link {

    /** ------------------------------ */

    private Node source;
    private Node destination;
    private Integer travelCost;
    /** ------------------------------ */

    public Link(Node source, Node destination) {
        this(source, destination, (source.getCoordinates() != null && destination.getCoordinates() != null) ?
                source.getCoordinates().distance(destination.getCoordinates()) : -1);
    }

    public Link(Node source, Node destination, Integer travelCost) {
        this.source = source;
        this.destination = destination;
        this.travelCost = travelCost;
        //Debug.log("Linking %d with %d (Tc: %d)", source.getId(), destination.getId(), travelCost);
    }

    /** ------------------------------ */

    public Node getDestination() {
        return this.destination;
    }

    /** ------------------------------ */

    public Node getSource() {
        return this.source;
    }

    public Integer getTravelCost() {
        return this.travelCost;
    }

    /** ------------------------------ */

    public boolean isLoopback() {
        return (source.getId() == destination.getId());
    }

}
