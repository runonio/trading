package io.runon.trading.technical.analysis.hl;
/**
 * @author macle
 */
public class HighLowN {
    protected int initN = 100;
    protected int continueN = 20;

    public int getInitN() {
        return initN;
    }

    public void setInitN(int initN) {
        this.initN = initN;
    }

    public int getContinueN() {
        return continueN;
    }

    public void setContinueN(int continueN) {
        this.continueN = continueN;
    }
}
