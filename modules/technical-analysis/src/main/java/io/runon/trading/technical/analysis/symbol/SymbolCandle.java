package io.runon.trading.technical.analysis.symbol;

import io.runon.trading.technical.analysis.candle.Candles;

/**
 * 종목 구분 기화와 캔들
 * @author macle
 */
public interface SymbolCandle extends Candles {
    SymbolCandle[] EMPTY_ARRAY = new SymbolCandle[0];

    String getSymbol();
}
