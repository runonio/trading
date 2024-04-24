package io.runon.trading.data.candle;

import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * 시간을 이용해서 캔들얻기
 * 백테스팅용
 * @author macle
 */
public interface TimeCandleLoad {

    TradeCandle getCandle(long time);
}
