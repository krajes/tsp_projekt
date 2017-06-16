package bridge;

import exceptions.DuplicatedNodeException;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public class NodeConnectionInfo implements Serializable {
    private Integer source;
    private ArrayList<Pair<Integer, Integer>> destinationList;

    public NodeConnectionInfo(Integer source) {
        this.source = source;
        this.destinationList = new ArrayList<>();
    }

    public void addConnection(Integer destination, Integer travelCost) throws DuplicatedNodeException {
        if (isConnectedTo(destination)) {
            throw new DuplicatedNodeException();
        }

        this.destinationList.add(new Pair<Integer, Integer>(destination, travelCost));
    }

    public boolean isConnectedTo(Integer destinationId) {
        for (Pair<Integer, Integer> destination : destinationList) {
            if (destination.getKey() == destinationId) {
                return true;
            }
        }

        return false;
    }

    public void details() {
        System.out.println("* Details for connections of id " + this.source);
        for(Pair<Integer, Integer> pair : this.destinationList) {
            System.out.printf("  Destination: %d + Cost: %d\n", pair.getKey(), pair.getValue());
        }
    }

    public Integer getSource() {
        return source;
    }

    public ArrayList<Pair<Integer, Integer>> getDestinationList() {
        return destinationList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeConnectionInfo that = (NodeConnectionInfo) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        return destinationList != null ? destinationList.equals(that.destinationList) : that.destinationList == null;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (destinationList != null ? destinationList.hashCode() : 0);
        return result;
    }
}
