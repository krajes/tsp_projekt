package bridge;

import exceptions.DuplicatedNodeException;
import org.junit.Test;
import testing.AbstractTest;

import static org.junit.Assert.assertEquals;

public class NodeConnectionInfoTest extends AbstractTest {
    @Test
    public void testNodeConnection() {
        try {
            NodeConnectionInfo nodeConnectionInfo = new NodeConnectionInfo(1);
            nodeConnectionInfo.addConnection(2, 125);
            nodeConnectionInfo.addConnection(3, 250);
            assertEquals(2, nodeConnectionInfo.getDestinationList().size());
            assertEquals(new Integer(2), nodeConnectionInfo.getDestinationList().get(0).getKey());
            assertEquals(new Integer(125), nodeConnectionInfo.getDestinationList().get(0).getValue());
        } catch (DuplicatedNodeException e) {
            failException(e);
        }
    }

    @Test(expected = DuplicatedNodeException.class)
    public void testNodeConnectionDuplicate() throws DuplicatedNodeException {
        NodeConnectionInfo nodeConnectionInfo = new NodeConnectionInfo(1);
        nodeConnectionInfo.addConnection(2, 150);
        nodeConnectionInfo.addConnection(2, 250);
    }
}