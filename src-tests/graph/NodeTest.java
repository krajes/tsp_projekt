package graph;

import exceptions.DuplicatedNodeException;
import exceptions.NodeConnectedException;
import org.junit.Before;
import org.junit.Test;
import testing.AbstractTest;

import static org.junit.Assert.*;

public class NodeTest extends AbstractTest {
    private final Integer NODE_ID = 1;
    private final Integer NODE_COST = 10;
    private Graph graph = null;
    private Node defaultNode = null;

    @Before
    public void init() {
        graph = new Graph();

        try {
            graph.addNode(NODE_ID, NODE_COST);
            defaultNode = graph.getNode(NODE_ID);
        } catch (DuplicatedNodeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDefaultNodeCount() {
        assertEquals(1, graph.getNodeList().size());
    }

    @Test
    public void testDefaultNodeId() {
        assertEquals(NODE_ID, defaultNode.getId());
    }

    @Test
    public void testDefaultNodeCost() {
        assertEquals(NODE_COST, defaultNode.getCost());
    }

    @Test
    public void testDefaultNodeCoordinates() {
        assertNull(defaultNode.getCoordinates());
    }

    @Test
    public void testDefaultNodeLinks() {
        assertTrue(defaultNode.getLinks().isEmpty());
    }

    @Test
    public void testDefaultNodeNonExistingLink() {
        assertNull(defaultNode.getLink(2));
    }

    @Test
    public void testNodeCoordinatesAfterSet() {
        try {
            Coordinates2D coordinates = new Coordinates2D(50, 100);
            graph.addNode(2, 10, coordinates.getX(), coordinates.getY());
            Node nodeWithCoordinates = graph.getNode(2);
            assertTrue(nodeWithCoordinates.getCoordinates().equals(coordinates));
        } catch (DuplicatedNodeException e) {
            failException(e);
        }
    }

    @Test(expected = DuplicatedNodeException.class)
    public void testDuplicateNode() throws DuplicatedNodeException {
        Node duplicatedNode = new Node(NODE_ID, NODE_COST, graph);
    }

    @Test
    public void testNodeLinkingOneDirection() {
        try {
            graph.addNode(NODE_ID + 1, NODE_COST);
            Node secondNode = graph.getNode(NODE_ID + 1);

            assertTrue(defaultNode.linkWith(secondNode.getId(), 10, false));
            assertEquals(1, defaultNode.getLinks().size());
            assertEquals(0, secondNode.getLinks().size());
        } catch (DuplicatedNodeException | NodeConnectedException  e) {
            failException(e);
        }
    }

    @Test
    public void testNodeLinkingBiDirectional() {
        try {
            graph.addNode(NODE_ID + 1, NODE_COST);
            Node secondNode = graph.getNode(NODE_ID + 1);

            assertTrue(defaultNode.linkWith(secondNode.getId(), 10, true));
            assertEquals(1, defaultNode.getLinks().size());
            assertEquals(1, secondNode.getLinks().size());
        } catch (DuplicatedNodeException | NodeConnectedException  e) {
            failException(e);
        }
    }
}