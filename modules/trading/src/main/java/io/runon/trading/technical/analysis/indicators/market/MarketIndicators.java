package io.runon.trading.technical.analysis.indicators.market;

import io.runon.commons.parallel.ParallelIntegerSeqNext;
import io.runon.commons.parallel.ParallelNormalJob;
import io.runon.commons.parallel.ParallelWork;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.IdCandleTimes;
import io.runon.trading.technical.analysis.candle.IdCandlesGet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 시장 관련 지표
 * @author macle
 */
public abstract class MarketIndicators<T> {


    protected IdCandlesGet[] idCandles;
    protected long [] times = null;

    protected int searchLength = 50;

    public void setSearchLength(int searchLength) {
        this.searchLength = searchLength;
    }

    public MarketIndicators(IdCandlesGet[] idCandles){
        setIdCandles(idCandles);
    }


    public void setIdCandles(IdCandlesGet[] idCandles) {
        this.idCandles = idCandles;
        times = Candles.getTimes(idCandles);
    }

    public MarketIndicators(IdCandleTimes idCandleTimes){
        this.idCandles = idCandleTimes.getIdCandles();
        times = idCandleTimes.getTimes();
    }


    public MarketIndicators(IdCandlesGet[] idCandles, long [] times){
        this.idCandles = idCandles;
        this.times = times;
    }






    protected int scale = 4;
    public void setScale(int scale) {
        this.scale = scale;
    }


    protected BigDecimal minAmount = null;

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public IdCandlesGet[] getIdCandles() {
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

    protected abstract T [] newEmptyArray(int length);

    public T [] newArray(final int startIndex, int end){

        //병렬처리로 변경
        
        final T [] array = newEmptyArray(end - startIndex);


        ParallelWork<Integer> parallelWork = integer -> {
            array[integer] = getData(integer + startIndex);
        };


        ParallelIntegerSeqNext next = new ParallelIntegerSeqNext(0, array.length);

        ParallelNormalJob<Integer> job = new ParallelNormalJob<>(parallelWork, next);
        job.runSync();
//        for (int i = 0; i < array.length ; i++) {
//            array[i] = getData(i + startIndex);
//        }


        // null 처리
        List<T> list = new ArrayList<>();
        for (T data : array){
            if(data == null){
                continue;
            }
            list.add(data);
        }

        return list.toArray(newEmptyArray(0));
    }




    protected int searchIndex(int index){
        return (times.length - index) + this.searchLength;
    }
}
