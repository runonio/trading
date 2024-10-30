package io.runon.trading.data.management;

import io.runon.trading.data.Futures;

/**
 * @author macle
 */
public interface FuturesPathLastTime {


    FuturesPathLastTime CANDLE = new FuturesPathLastTimeCandle();
    long getLastTime(Futures futures, String interval);

    String getFilesDirPath(Futures futures, String interval);
     String getLastTimeFilePath(Futures futures, String interval);
}
