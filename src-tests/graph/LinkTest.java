package graph;

import exceptions.DuplicatedNodeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinkTest {
    private Graph graph = null;
    private Node node1 = null;
    private Node node2 = null;
    private Node node1Coordinates = null;
    private Node node2Coordinates = null;

    @Before
    public void init() {
        graph = new Graph();

        try {
            node1 = graph.addNode(1, 10);
            node2 = graph.addNode(2, 15);

            node1Coordinates = graph.addNode(3, 10, 15, 30);
            node2Coordinates = graph.addNode(4, 15, 25, 40);
        } catch (DuplicatedNodeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLinkWithNoCoordinates() {
        Link link = new Link(node1, node2);
        assertEquals(node1, link.getSource());
        assertEquals(node2, link.getDestination());
        assertEquals(new Integer(-1), link.getTravelCost());
        assertFalse(link.isLoopback());
    }

    @Test
    public void testLoopbackLink() {
        Link link = new Link(node1, node1, 5);
        assertTrue(link.isLoopback());
    }

    @Test
    public void testLinkWithCoordinates() {
        Link link = new Link(node1Coordinates, node2Coordinates);
        assertEquals(new Integer(14), link.getTravelCost());
    }
}