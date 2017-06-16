package algorithm;

import bridge.OutputData;
import exceptions.DuplicatedNodeException;
import exceptions.NodeConnectedException;
import graph.Graph;
import graph.Node;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class NaiveTest {
    private Graph graph = null;

    public void init() {
        graph = new Graph();
        try {
            Node node1 = graph.addNode(1, 0);
            Node node2 = graph.addNode(2, 0);
            Node node3 = graph.addNode(3, 0);

            node1.linkWith(2, 10, true);
            node1.linkWith(3, 15, true);
            node2.linkWith(3, 20, true);
        } catch (DuplicatedNodeException | NodeConnectedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAlgorithm() {
        for (int i = 1; i <= 1000; ++i) {
            init();
            Naive naive = new Naive(graph);

            List<Integer> correctResult1 = Arrays.asList(1, 2, 3, 1);
            List<Integer> correctResult2 = Arrays.asList(1, 3, 2, 1);
            Integer correctCost = 45;
            Pair<ArrayList<Integer>, Integer> result = naive.generateSequence(1);

            OutputData outputData = naive.getOutputData();

            assertThat(result.getKey(), anyOf(equalTo(correctResult1), equalTo(correctResult2)));
            assertThat(outputData.getCycle(), anyOf(equalTo(correctResult1), equalTo(correctResult2)));
            assertEquals(result.getValue(), correctCost);
            assertEquals(correctCost, outputData.getMinimumCost());
            assertEquals(correctCost, outputData.getFirstComputedCost());
            assertTrue(outputData.getComparisons() <= 1001);
        }
    }
}