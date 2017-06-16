/**
 * - - - - - - - - - - - - - - - - - - - - - - - - - -
 * P A C K A G E: graph
 * I N F O:
 * <p>
 * - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package algorithm;

import bridge.OutputData;
import graph.Graph;
import graph.Node;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Naive {
    private Graph graph;
    private Random random = new Random();
    private static final Integer rounds = 1000;
    private OutputData outputData;

    public Naive(Graph graph) {
        this.graph = graph;
        this.outputData = new OutputData();
    }

    public OutputData getOutputData() {
        return this.outputData;
    }

    public Pair<ArrayList<Integer>, Integer> generateSequence(Integer root) {
        Node rootNode = this.graph.getNode(root), previousNode, nextNode;
        if(rootNode == null) return null;

        Integer lastCost, bestCost = Integer.MAX_VALUE;
        ArrayList<Integer> bestCycle = new ArrayList<>(), cycle = new ArrayList<>();
        ArrayList<Node> nodesToProcess;

        boolean firstRun = true;
        for(int i = Naive.rounds; i >= 0; --i) {

            cycle.clear();
            nodesToProcess = graph.getNodeList();
            nodesToProcess.remove(rootNode);
            lastCost = 0;
            previousNode = rootNode;
            cycle.add(rootNode.getId());

            this.outputData.incrementComparisions();

            while(!nodesToProcess.isEmpty()) {
                nextNode = nodesToProcess.get(random.nextInt(nodesToProcess.size()));
                cycle.add(nextNode.getId());
                nodesToProcess.remove(nextNode);
                try {
                    lastCost += previousNode.getLink(nextNode.getId()).getTravelCost() + nextNode.getCost();
                } catch (NullPointerException npe) {
                    lastCost += 0;
                }
                previousNode = nextNode;
            }

            cycle.add(rootNode.getId());
            lastCost += previousNode.getLink(rootNode.getId()).getTravelCost() + rootNode.getCost();

            if(firstRun) {
                firstRun = false;
                this.outputData.setFirstComputedCost(lastCost);
            }

            if(bestCost >= lastCost) {
                bestCost = lastCost;
                bestCycle = new ArrayList<>(cycle);
            }
        }

        this.outputData.setMinimumCost(bestCost);
        Iterator<Integer> cycleIterator = bestCycle.iterator();
        while(cycleIterator.hasNext())
            this.outputData.putToCycle(cycleIterator.next());

        return new Pair<ArrayList<Integer>, Integer>(bestCycle, bestCost);
    }
}
