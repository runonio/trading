package io.runon.trading.backtesting;

import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * 백테스팅용 캔들 얻기
 * @author macle
 */
public interface CandlesGet {
    TradeCandle[] getCandles(long time, long endTime, int count);
}
