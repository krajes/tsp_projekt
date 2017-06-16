package bridge;

import org.junit.Test;

import static org.junit.Assert.*;

public class OutputDataTest {
    @Test
    public void testEmptyOutputData() {
        OutputData outputData = new OutputData();
        assertEquals(new Integer(0), outputData.getComparisons());
        assertEquals(new Integer(0), outputData.getFirstComputedCost());
        assertEquals(new Integer(0), outputData.getMinimumCost());
        assertTrue(outputData.getCycle().isEmpty());
    }

    @Test
    public void testOutputDataAfterChanges() {
        OutputData outputData = new OutputData();
        outputData.setComparisons(10);
        outputData.setFirstComputedCost(500);
        outputData.setMinimumCost(100);
        outputData.putToCycle(1);
        outputData.putToCycle(2);

        assertEquals(new Integer(10), outputData.getComparisons());
        assertEquals(new Integer(500), outputData.getFirstComputedCost());
        assertEquals(new Integer(100), outputData.getMinimumCost());
        assertEquals(2, outputData.getCycle().size());
        assertEquals(new Integer(1), outputData.getCycle().get(0));
        assertEquals(new Integer(2), outputData.getCycle().get(1));
    }
}