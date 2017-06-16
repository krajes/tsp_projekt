package bridge;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DataToServerTest {
    @Test
    public void testDataToServer() {
        ArrayList<Integer> nodeList = new ArrayList<>();
        nodeList.addAll(Arrays.asList(0, 1, 2, 3));

        ArrayList<NodeConnectionInfo> nodeConnectionInfos = new ArrayList<>();
        nodeConnectionInfos.add(new NodeConnectionInfo(0));
        nodeConnectionInfos.add(new NodeConnectionInfo(1));
        nodeConnectionInfos.add(new NodeConnectionInfo(2));
        nodeConnectionInfos.add(new NodeConnectionInfo(3));

        DataToServer dataToServer = new DataToServer(1, nodeList, nodeConnectionInfos);
        assertEquals(4, dataToServer.getNodeConnectionInfos().size());
        assertEquals(4, dataToServer.getNodeList().size());
        assertEquals(new Integer(0), dataToServer.getNodeConnectionInfos().get(0).getSource());
        assertEquals(new Integer(1), dataToServer.getNodeList().get(1));
        assertEquals(new Integer(1), dataToServer.getRootNodeId());
    }
}