package io.runon.trading.technical.analysis.candle;

/**
 * 종목 구분 기화, 캔들, 시간데이터
 * @author macle
 */
public class IdCandleTimes {

    private String id;

    private final IdCandlesGet[] idCandles;
    private final long [] times;
    public IdCandleTimes(IdCandlesGet[] idCandles){
        this.idCandles = idCandles;
        times = Candles.getTimes(idCandles, true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdCandlesGet[] getIdCandles() {
        return idCandles;
    }

    public long[] getTimes() {
        return times;
    }
}
