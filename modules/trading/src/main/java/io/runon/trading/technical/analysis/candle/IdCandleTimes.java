package io.runon.trading.technical.analysis.candle;

/**
 * 종목 구분 기화, 캔들, 시간데이터
 * @author macle
 */
public class IdCandleTimes {

    private String id;

    private final IdCandles[] symbolCandles;
    private final long [] times;
    public IdCandleTimes(IdCandles[] symbolCandles){
        this.symbolCandles = symbolCandles;
        times = Candles.getTimes(symbolCandles);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdCandles[] getSymbolCandles() {
        return symbolCandles;
    }

    public long[] getTimes() {
        return times;
    }
}
