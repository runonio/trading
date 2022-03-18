package io.runon.trading.backtesting;

import io.runon.trading.Candle;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public abstract class CandleSymbolMapPrice implements CandleSymbolPrice {

    protected final Map<String, Candle> candleMap = new HashMap<>();

    @Override
    public void setCandle(String symbol, Candle candle){
        candleMap.put(symbol, candle);
    }

    @Override
    public BigDecimal getPrice(String symbol) {
        return candleMap.get(symbol).getClose();
    }
}
