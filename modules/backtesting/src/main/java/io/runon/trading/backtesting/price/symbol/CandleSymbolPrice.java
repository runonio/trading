package io.runon.trading.backtesting.price.symbol;

import io.runon.trading.Candle;

/**
 * 백테스팅에서 사용하는 가격용 캔들
 * @author macle
 */
public interface CandleSymbolPrice extends SymbolPrice {

    void setPrice(String symbol, Candle candle);
}
