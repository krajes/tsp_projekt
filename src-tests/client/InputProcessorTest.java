package client;

import bridge.NodeConnectionInfo;
import exceptions.DuplicatedNodeException;
import org.junit.Test;
import testing.AbstractTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InputProcessorTest extends AbstractTest {
    @Test
    public void testFileLoad() {
        List<Integer> expectedNodeList = Arrays.asList(0, 1, 2, 3);
        List<NodeConnectionInfo> expectedNodeConnectionInfo = new ArrayList<>();

        try {
            NodeConnectionInfo node0 = new NodeConnectionInfo(0);
            node0.addConnection(1, 263);
            node0.addConnection(2, 433);
            node0.addConnection(3, 355);

            NodeConnectionInfo node1 = new NodeConnectionInfo(1);
            node1.addConnection(0, 663);
            node1.addConnection(2, 588);
            node1.addConnection(3, 485);

            NodeConnectionInfo node2 = new NodeConnectionInfo(2);
            node2.addConnection(0, 433);
            node2.addConnection(1, 788);
            node2.addConnection(3, 699);

            NodeConnectionInfo node3 = new NodeConnectionInfo(3);
            node3.addConnection(0, 123);
            node3.addConnection(1, 543);
            node3.addConnection(2, 255);

            expectedNodeConnectionInfo.add(node0);
            expectedNodeConnectionInfo.add(node1);
            expectedNodeConnectionInfo.add(node2);
            expectedNodeConnectionInfo.add(node3);

            InputProcessor.parseFile("test-data.json");
            assertEquals(expectedNodeList, InputProcessor.getNodeList());
            assertEquals(expectedNodeConnectionInfo, InputProcessor.getNodeConnectionInfo());
        } catch (DuplicatedNodeException e) {
            failException(e);
        }
    }
}