package io.runon.trading.technical.analysis.candle;

import com.seomse.commons.data.BeginEnd;
import com.seomse.commons.data.BeginEndImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * technical analysis candle 관련 유틸성 클래스
 * 기술적 분석 캔들 관련  유틸성 매소드 정리
 * @author macle
 */
public class TaCandles {

    public static long [] getTimes(Candles [] array){
        Set<Long> timeSet = new HashSet<>();
        for(Candles candles : array){
            TradeCandle [] tradeCandles = candles.getCandles();
            for(TradeCandle candle: tradeCandles){
                timeSet.add(candle.getTime());
            }
        }

        long [] times = new long[timeSet.size()];
        int index = 0;
        for (long time : timeSet) {
            times[index++] = time;
        }

        Arrays.sort(times);

        return times;
    }

    //빈켄들이 있을경우 시간값에 대한 인덱스를 가져오지 못할경우 가까운 시간값 활용
    public static BeginEnd getNearTimeRange(TradeCandle [] candles, long openTime, long closeTime){

        if(candles.length < 1){
            return new BeginEndImpl(-1, -1);
        }

        TradeCandle candle = candles[0];

        long candleTime = candle.getCloseTime() - candle.getOpenTime();

        return getNearTimeRange(candles, candleTime, openTime, closeTime);
    }

    //빈켄들이 있을경우 시간값에 대한 인덱스를 가져오지 못할경우 가까운 시간값 활용
    public static BeginEnd getNearTimeRange(TradeCandle [] candles, long candleTime, long openTime, long closeTime){

        int openTimeIndex= getNearOpenTimeIndex(candles, candleTime, openTime);
        int closeTimeIndex = getNearCloseTimeIndex(candles, candleTime, closeTime);

        return new BeginEndImpl(openTimeIndex, closeTimeIndex+1);
    }

    public static int getNearOpenTimeIndex(TradeCandle [] candles, long candleTime, long time){

        long lastOpenTime = candles[candles.length-1].getOpenTime();

        if(time >= lastOpenTime){
            return candles.length - 1;
        }

        int searchLength = (int)((lastOpenTime- time)/candleTime) + 1;
        return getNearOpenTimeIndex(candles, time, searchLength);
    }
    public static int getNearCloseTimeIndex(TradeCandle [] candles, long candleTime, long time){

        long lastCloseTime = candles[candles.length-1].getCloseTime();

        if(time >= lastCloseTime){
            return candles.length - 1;
        }

        long lastOpenTime = candles[candles.length-1].getOpenTime();
        if(lastOpenTime < time){
            return candles.length - 1;
        }

        int searchLength = (int)((lastCloseTime - time)/candleTime) + 1;

        return getNearCloseTimeIndex(candles, time, searchLength);
    }



    public static int getOpenTimeIndex(TradeCandle [] candles, long openTime){
        if(candles.length < 1){
            return -1;
        }

        TradeCandle candle = candles[0];

        long candleTime = candle.getCloseTime() - candle.getOpenTime();

        return getOpenTimeIndex(candles, candleTime, openTime);
    }

    public static int getOpenTimeIndex(TradeCandle [] candles, long candleTime, long time){

        long lastOpenTime = candles[candles.length-1].getOpenTime();

        if(time >= lastOpenTime){
            if(candles[candles.length-1].getCloseTime() > time ) {
                return candles.length - 1;
            }else{
                return -1;
            }
        }

        int searchLength = (int)((lastOpenTime- time)/candleTime) + 1;
        return getOpenTimeIndex(candles, time, searchLength);
    }

    public static int getCloseTimeIndex(TradeCandle [] candles, long candleTime, long time){

        long lastCloseTime = candles[candles.length-1].getCloseTime();

        if(time >= lastCloseTime){
            return -1;
        }

        long lastOpenTime = candles[candles.length-1].getOpenTime();
        if(lastOpenTime <= time){
            return candles.length - 1;
        }


        int searchLength = (int)((lastCloseTime - time)/candleTime) + 1;

        return getCloseTimeIndex(candles, time, searchLength);
    }

    public static int getCloseTimeIndex(TradeCandle [] candles, long closeTime, int searchLength){
        int startIndex = candles.length - searchLength;
        if(startIndex < 0){
            startIndex = 0;
        }

        for (int i = startIndex; i < candles.length; i++) {

            if(candles[i].getOpenTime() < closeTime && candles[i].getCloseTime() >= closeTime){
                return i;
            }

            if(candles[i].getOpenTime() >= closeTime){
                return -1;
            }

        }
        return -1;
    }


    public static int getOpenTimeIndex(TradeCandle [] candles, long openTime, int searchLength){

        int startIndex = candles.length - searchLength;
        if(startIndex < 0){
            startIndex = 0;
        }

        for (int i = startIndex; i < candles.length; i++) {
            if(candles[i].getOpenTime() <= openTime && candles[i].getCloseTime() > openTime){
                return i;
            }

            if(candles[i].getOpenTime() > openTime){
                return -1;
            }
        }

        return -1;
    }

    public static int getNearOpenTimeIndex(TradeCandle [] candles, long openTime, int searchLength){

        int startIndex = candles.length - searchLength;
        if(startIndex < 0){
            startIndex = 0;
        }

        for (int i = startIndex; i < candles.length; i++) {
            if(openTime == candles[i].getOpenTime()){
                return i;
            }

            if(candles[i].getOpenTime() <= openTime && candles[i].getCloseTime() > openTime){
                return i;
            }

            if(candles[i].getOpenTime() > openTime){
                return i-1;
            }

        }
        return candles.length -1;
    }

    public static int getNearCloseTimeIndex(TradeCandle [] candles, long closeTime, int searchLength){

        int startIndex = candles.length - searchLength;
        if(startIndex < 0){
            startIndex = 0;
        }

        for (int i = startIndex; i < candles.length; i++) {
            if(candles[i].getOpenTime() < closeTime && candles[i].getCloseTime() >= closeTime){
                return i;
            }

            if(candles[i].getOpenTime() > closeTime){
                return i-1;
            }

        }
        return candles.length -1;
    }



    public static BigDecimal high(CandleStick [] candles, int n){
        return high(candles,n, candles.length-1);
    }
    public static BigDecimal high(CandleStick [] candles, int n, int index){
        int end = index+1;
        int startIndex = end -n;
        if(end > candles.length){
            end = candles.length;
        }

        if(startIndex < 0){
            startIndex = 0;
        }

        BigDecimal high = candles[startIndex].getHigh();

        for (int i = startIndex+1; i <end ; i++) {
            BigDecimal compareHigh = candles[i].getHigh();
            if(compareHigh.compareTo(high) > 0){
                high = compareHigh;
            }
        }

        return high;
    }

    public static BigDecimal low(CandleStick [] candles, int n){
        return low(candles, n, candles.length-1);
    }

    public static BigDecimal low(CandleStick [] candles, int n, int index){
        int end = index+1;
        int startIndex = end -n;
        if(end > candles.length){
            end = candles.length;
        }

        if(startIndex < 0){
            startIndex = 0;
        }

        BigDecimal low = candles[startIndex].getLow();

        for (int i = startIndex+1; i <end ; i++) {
            BigDecimal compareLow = candles[i].getLow();
            if(compareLow.compareTo(low) < 0){
                low = compareLow;
            }
        }

        return low;
    }




}
