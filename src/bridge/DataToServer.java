package bridge;

import java.io.Serializable;
import java.util.ArrayList;

public class DataToServer implements Serializable {
    private Integer rootNodeId;
    private ArrayList<Integer> nodeList;
    private ArrayList<NodeConnectionInfo> nodeConnectionInfos;

    public DataToServer(Integer rootNodeId, ArrayList<Integer> nodeList, ArrayList<NodeConnectionInfo> nodeConnectionInfos) {
        this.rootNodeId = rootNodeId;
        this.nodeList = nodeList;
        this.nodeConnectionInfos = nodeConnectionInfos;
    }

    public void debugPrint() {
        System.out.println("Data to be sent:");
        System.out.println("* Root node id: " + rootNodeId);
        System.out.println("* Node list:"); for(Integer nodeId : this.nodeList) System.out.print(nodeId + ", ");
        System.out.println("\n* Node connection details:"); for(NodeConnectionInfo nci : nodeConnectionInfos) nci.details();
    }

    public Integer getRootNodeId() {
        return rootNodeId;
    }

    public ArrayList<Integer> getNodeList() {
        return nodeList;
    }

    public ArrayList<NodeConnectionInfo> getNodeConnectionInfos() {
        return nodeConnectionInfos;
    }
}
