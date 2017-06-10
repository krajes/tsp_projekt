package algorithm;

import graph.Graph;
import graph.Node;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Naive {
    private Graph graph;
    private Random random = new Random();
    private static final Integer rounds = 1000;

    public Naive(Graph graph) {
        this.graph = graph;
    }

    public Pair<ArrayList<Integer>, Integer> generateSequence(Integer root) {
        Node rootNode = this.graph.getNode(root), previousNode, nextNode;
        if(rootNode == null) return null;

        Integer lastCost, bestCost = Integer.MAX_VALUE;
        ArrayList<Integer> bestCycle = new ArrayList<>(), cycle = new ArrayList<>();
        ArrayList<Node> nodesToProcess;

        for(int i = Naive.rounds; i >= 0; --i) {

            cycle.clear();
            nodesToProcess = graph.getNodeList();
            nodesToProcess.remove(rootNode);
            lastCost = 0;
            previousNode = rootNode;
            cycle.add(rootNode.getId());


            while(!nodesToProcess.isEmpty()) {
                nextNode = nodesToProcess.get(random.nextInt(nodesToProcess.size()));
                cycle.add(nextNode.getId());
                nodesToProcess.remove(nextNode);
                lastCost += previousNode.getCoordinates().distance(nextNode.getCoordinates());
                previousNode = nextNode;
            }

            cycle.add(rootNode.getId());
            lastCost += previousNode.getCoordinates().distance(rootNode.getCoordinates());

            if(bestCost >= lastCost) {
                bestCost = lastCost;
                bestCycle = new ArrayList<>(cycle);
            }
        }

        return new Pair<ArrayList<Integer>, Integer>(bestCycle, bestCost);
    }
}
