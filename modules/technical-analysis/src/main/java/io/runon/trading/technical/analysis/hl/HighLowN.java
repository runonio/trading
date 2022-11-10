package io.runon.trading.technical.analysis.hl;

import com.seomse.commons.utils.time.Times;

/**
 * @author macle
 */
public class HighLowN {
    protected int initN = 100;
    protected int continueN = 20;

    protected long candleTime = Times.MINUTE_1;

    public void setCandleTime(long candleTime) {
        this.candleTime = candleTime;
    }

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
