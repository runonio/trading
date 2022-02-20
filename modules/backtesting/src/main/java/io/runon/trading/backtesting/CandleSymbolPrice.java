package io.runon.trading.backtesting;

import io.runon.trading.Candle;
import io.runon.trading.SymbolPrice;
/**
 * 백테스팅에서 사용하는 가격용 캔들
 * @author macle
 */
public interface CandleSymbolPrice extends SymbolPrice {

    void setCandle(String symbol, Candle candle);
}
