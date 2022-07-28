package io.runon.trading.technical.analysis;

import io.runon.trading.technical.analysis.candle.Candles;

/**
 * 종목 구분 기화와 캔들
 * @author macle
 */
public interface SymbolCandle extends Candles {
    String getSymbol();
}
