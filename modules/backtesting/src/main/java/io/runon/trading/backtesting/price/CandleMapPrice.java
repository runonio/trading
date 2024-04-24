package io.runon.trading.backtesting.price;

import io.runon.trading.Candle;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public abstract class CandleMapPrice implements CandlePrice {

    protected final Map<String, Candle> candleMap = new HashMap<>();

    @Override
    public void setPrice(String symbol, Candle candle){
        candleMap.put(symbol, candle);
    }

    @Override
    public BigDecimal getPrice(String id) {
        return candleMap.get(id).getClose();
    }
}
