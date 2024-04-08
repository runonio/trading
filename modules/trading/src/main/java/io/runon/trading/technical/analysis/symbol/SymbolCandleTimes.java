package io.runon.trading.technical.analysis.symbol;

import io.runon.trading.technical.analysis.candle.Candles;

/**
 * 종목 구분 기화, 캔들, 시간데이터
 * @author macle
 */
public class SymbolCandleTimes {

    private String id;

    private final SymbolCandle[] symbolCandles;
    private final long [] times;
    public SymbolCandleTimes(SymbolCandle[] symbolCandles){
        this.symbolCandles = symbolCandles;
        times = Candles.getTimes(symbolCandles);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SymbolCandle[] getSymbolCandles() {
        return symbolCandles;
    }

    public long[] getTimes() {
        return times;
    }
}
