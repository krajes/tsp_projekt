package graph;

import exceptions.DuplicatedNodeException;
import exceptions.NodeConnectedException;
import org.junit.Before;
import org.junit.Test;
import testing.AbstractTest;

import static org.junit.Assert.*;

public class GraphTest extends AbstractTest {
    private Graph graph = null;

    @Before
    public void init() {
        graph = new Graph();
    }

    @Test
    public void testNodeListCount() {
        assertEquals(0, graph.getNodeList().size());
    }

    @Test
    public void testNodeIdsCount() {
        assertEquals(0, graph.getNodeIds().size());
    }

    @Test
    public void testNonExistingNode() {
        assertNull(graph.getNode(1));
        assertFalse(graph.hasNode(1));
    }

    @Test
    public void testCoherencyForEmptyGraph() {
        assertFalse(graph.coherencyTest());
    }

    @Test
    public void testGraphNodeAddition() {
        try {
            graph.addNode(1, 10);
            assertEquals(1, graph.getNodeList().size());
            assertTrue(graph.hasNode(1));
            assertNotNull(graph.getNode(1));
            assertTrue(graph.coherencyTest());
        } catch (DuplicatedNodeException e) {
            failException(e);
        }
    }

    @Test(expected = DuplicatedNodeException.class)
    public void testDuplicateNode() throws DuplicatedNodeException {
        graph.addNode(1, 10);
        graph.addNode(1, 15);
    }

    @Test
    public void testNodeConnection() {
        try {
            graph.addNode(1, 10);
            graph.addNode(2, 15);
            assertTrue(graph.connectNodes(1, 2, 10, false));
            assertFalse(graph.coherencyTest());
            graph.getNode(2).linkWith(1, 15, false);
            assertTrue(graph.coherencyTest());
        } catch (DuplicatedNodeException | NodeConnectedException  e) {
            failException(e);
        }
    }
}