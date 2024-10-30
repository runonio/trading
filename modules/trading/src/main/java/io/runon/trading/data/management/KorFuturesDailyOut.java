package io.runon.trading.data.management;

import io.runon.trading.data.file.PathTimeLine;

/**
 * @author macle
 */
public abstract class KorFuturesDailyOut implements FuturesDailyOutParam{


    protected final FuturesPathLastTime pathLastTime;
    protected final PathTimeLine pathTimeLine;

    protected FuturesDailyOut dailyOut;

    public KorFuturesDailyOut(FuturesPathLastTime pathLastTime, PathTimeLine pathTimeLine){
        this.pathLastTime = pathLastTime;
        this.pathTimeLine = pathTimeLine;
        dailyOut = new FuturesDailyOut(this);
    }


    @Override
    public PathTimeLine getPathTimeLine() {
        return pathTimeLine;
    }

    @Override
    public FuturesPathLastTime getFuturesPathLastTime() {
        return pathLastTime;
    }

    @Override
    public int getNextDay() {
        return 100;
    }

    @Override
    public String getExchange() {
        return "KRX";
    }
}
