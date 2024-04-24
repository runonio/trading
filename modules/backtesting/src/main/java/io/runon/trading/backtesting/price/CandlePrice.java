package io.runon.trading.backtesting.price;

import io.runon.trading.Candle;

/**
 * 백테스팅에서 사용하는 가격용 캔들
 * @author macle
 */
public interface CandlePrice extends IdPrice {

    void setPrice(String symbol, Candle candle);
}
