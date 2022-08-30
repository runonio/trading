package io.runon.trading.technical.analysis.market;

import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;

import java.math.BigDecimal;

/**
 * 시장 관련 지표
 * @author macle
 */
public abstract class MarketIndicator<T> {


    protected SymbolCandle[] symbolCandles;
    protected long [] times = null;

    protected int searchLength = 50;

    public void setSearchLength(int searchLength) {
        this.searchLength = searchLength;
    }

    public MarketIndicator(SymbolCandle[] symbolCandles){
        setSymbolCandles(symbolCandles);
    }

    public void setSymbolCandles(SymbolCandle[] symbolCandles) {
        this.symbolCandles = symbolCandles;
        times = TaCandles.getTimes(symbolCandles);
    }

    protected int scale = 4;
    public void setScale(int scale) {
        this.scale = scale;
    }


    protected BigDecimal minTradingPrice = null;

    public void setMinTradingPrice(BigDecimal minTradingPrice) {
        this.minTradingPrice = minTradingPrice;
    }

    public SymbolCandle[] getSymbolCandles() {
        return symbolCandles;
    }

    public long[] getTimes() {
        return times;
    }

    public int getScale() {
        return scale;
    }

    public abstract T getData(int index);

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
