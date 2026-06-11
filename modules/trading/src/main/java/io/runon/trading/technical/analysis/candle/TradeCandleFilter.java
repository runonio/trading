package io.runon.trading.technical.analysis.candle;

public interface TradeCandleFilter {

    boolean isFiltering(TradeCandle candle);
}
