package bridge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class OutputData implements Serializable {
    private Integer comparisons;
    private Integer firstComputedCost;
    private Integer minimumCost;
    private ArrayList<Integer> cycle;

    public OutputData() {
        this.comparisons = 0;
        this.firstComputedCost = 0;
        this.minimumCost = 0;
        this.cycle = new ArrayList<>();
    }

    public Integer getComparisons() {
        return comparisons;
    }

    public void setComparisons(Integer comparisons) {
        this.comparisons = comparisons;
    }

    public Integer getFirstComputedCost() {
        return firstComputedCost;
    }

    public void setFirstComputedCost(Integer firstComputedCost) {
        this.firstComputedCost = firstComputedCost;
    }

    public Integer getMinimumCost() {
        return minimumCost;
    }

    public void setMinimumCost(Integer minimumCost) {
        this.minimumCost = minimumCost;
    }

    public ArrayList<Integer> getCycle() {
        return cycle;
    }

    public void putToCycle(Integer toAdd) {
        this.cycle.add(toAdd);
    }

    public void resetCycle() {
        this.cycle.clear();
    }

    public void incrementComparisions() {
        this.comparisons++;
    }

    @Override
    public String toString() {
        return "OutputData{" +
                "comparisons=" + comparisons +
                ", firstComputedCost=" + firstComputedCost +
                ", minimumCost=" + minimumCost +
                ", cycle=" + cycle +
                '}';
    }

    public String getFormatedCycle() {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> iterator = this.cycle.iterator();
        while(iterator.hasNext()) sb.append(String.format("%d ", iterator.next()));
        return sb.toString();
    }

    private static Random random = new Random();
    public void generateRandom() {
        final int bound = 9999;
        this.comparisons = random.nextInt(bound);
        this.minimumCost = random.nextInt(bound);
        this.firstComputedCost = this.minimumCost + random.nextInt(bound);
        for(int i = 0; i < 10; i++)
            this.cycle.add(2 * i);
    }
}
