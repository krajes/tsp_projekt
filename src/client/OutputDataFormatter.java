package client;

import bridge.OutputData;

public class OutputDataFormatter {

    private String patternString;
    private OutputData outputData;

    public OutputDataFormatter(OutputData outputData) {
        this.outputData = outputData;
    }

    public String getString() {
        return null;
    }

    public String getParsedString(String input) {
        this.patternString = input;
        this.parseString();
        return this.patternString;
    }

    /** -------------------------------------------------------------- Parser -------------- */

    private void parseString() {
        this.patternString = this.patternString.replaceAll(getPattern("MIN_COST"),
                String.format("%d", this.outputData.getMinimumCost()));
        this.patternString = this.patternString.replaceAll(getPattern("COMP_COUNT"),
                String.format("%d", this.outputData.getComparisons()));
        this.patternString = this.patternString.replaceAll(getPattern("FIRST_COST"),
                String.format("%d", this.outputData.getFirstComputedCost()));
        this.patternString = this.patternString.replaceAll(getPattern("CYCLE"), this.outputData.getFormatedCycle());
    }

    private String getPattern(String key) {
        return String.format("\\$\\{[ ]*%s[ ]*\\}", key);
    }

}
