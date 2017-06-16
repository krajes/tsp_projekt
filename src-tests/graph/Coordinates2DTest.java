package graph;

import org.junit.Test;

import static org.junit.Assert.*;

public class Coordinates2DTest {
    @Test
    public void testEmptyCoordinates() {
        Coordinates2D coordinates = new Coordinates2D();
        assertEquals(new Integer(0), coordinates.getX());
        assertEquals(new Integer(0), coordinates.getY());
    }

    @Test
    public void testCoordinatesDistance() {
        Coordinates2D coordinates1 = new Coordinates2D(10, 20);
        Coordinates2D coordinates2 = new Coordinates2D(20, 30);

        assertEquals(new Integer(0), coordinates1.distance(coordinates1));
        assertEquals(new Integer(14), coordinates1.distance(coordinates2));
        assertEquals(new Integer(14), coordinates2.distance(coordinates1));
    }

    @Test
    public void testCoordinatesMidTo() {
        Coordinates2D coordinates1 = new Coordinates2D(10, 20);
        Coordinates2D coordinates2 = new Coordinates2D(20, 30);
        Coordinates2D expectedMidPoint = new Coordinates2D(15, 25);

        assertEquals(expectedMidPoint, coordinates1.midTo(coordinates2));
    }

    @Test
    public void testNegativeCoordinatesMidTo() {
        Coordinates2D coordinates1 = new Coordinates2D(-10, -20);
        Coordinates2D coordinates2 = new Coordinates2D(-20, -30);
        Coordinates2D expectedMidPoint = new Coordinates2D(-15, -25);

        assertEquals(expectedMidPoint, coordinates1.midTo(coordinates2));

    }
}