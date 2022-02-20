package io.runon.trading.backtesting;

import io.runon.trading.Candle;
/**
 * 백테스팅에서 사용하는 가격용 캔들
 * @author macle
 */
public interface PriceCandle {

    Candle getPriceCandle();
}
