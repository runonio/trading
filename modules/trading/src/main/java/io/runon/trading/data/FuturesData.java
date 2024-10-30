package io.runon.trading.data;
/**
 * @author macle
 */
public interface FuturesData {
    Futures [] getListedFutures(String exchange, String ymd);

    Futures [] getFutures(String exchange, String ymd);

}
