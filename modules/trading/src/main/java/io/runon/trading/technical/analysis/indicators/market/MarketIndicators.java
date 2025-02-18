package io.runon.trading.technical.analysis.indicators.market;

import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.IdCandles;
import io.runon.trading.technical.analysis.candle.IdCandleTimes;

import java.math.BigDecimal;

/**
 * 시장 관련 지표
 * @author macle
 */
public abstract class MarketIndicators<T> {


    protected IdCandles[] idCandles;
    protected long [] times = null;

    protected int searchLength = 50;

    public void setSearchLength(int searchLength) {
        this.searchLength = searchLength;
    }

    public MarketIndicators(IdCandles[] idCandles){
        setIdCandles(idCandles);
    }

    public void setIdCandles(IdCandles[] idCandles) {
        this.idCandles = idCandles;
        times = Candles.getTimes(idCandles);
    }

    public MarketIndicators(IdCandleTimes symbolCandleTimes){
        this.idCandles = symbolCandleTimes.getIdCandles();
        times = symbolCandleTimes.getTimes();
    }


    protected int scale = 4;
    public void setScale(int scale) {
        this.scale = scale;
    }


    protected BigDecimal minAmount = null;

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public IdCandles[] getIdCandles() {
        return idCandles;
    }

    public long[] getTimes() {
        return times;
    }

    public int getScale() {
        return scale;
    }

    public abstract T getData(int index);

    public T [] getArray(){
        return getArray(0, times.length);
    }

    public T [] getArray(int resultLength){
        return getArray(times.length - resultLength, times.length);
    }

    public T [] getArray(int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > times.length){
            end = times.length;
        }

        if(startIndex >= end){
            //옵션을 잘 못 보
            throw new IllegalArgumentException("startIndex >= end  startIndex: " + startIndex +", end: " + end );
        }

        return  newArray(startIndex, end);
    }

    public abstract T [] newArray(int startIndex, int end);


    protected int searchIndex(int index){
        return (times.length - index) + this.searchLength;
    }
}
