package io.runon.trading.technical.analysis.candle;

import com.seomse.commons.data.BeginEnd;
import com.seomse.commons.data.BeginEndImpl;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.trading.TradingMath;
import io.runon.trading.TradingTimes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * technical analysis candle 관련 유틸성 클래스
 * 기술적 분석 캔들 관련  유틸성 매소드 정리
 * @author macle
 */
public class Candles {

    public static long [] getTimes(GetCandles[] array){
        Set<Long> timeSet = new HashSet<>();
        for(GetCandles candles : array){
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


    public static BigDecimal high(CandleStick [] candles){
        BigDecimal high = candles[0].getHigh();

        for (CandleStick candleStick : candles) {
            BigDecimal compareHigh = candleStick.getHigh();
            if(compareHigh.compareTo(high) > 0){
                high = compareHigh;
            }
        }

        return high;
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

    public static BigDecimal low(CandleStick [] candles){
        BigDecimal low = candles[0].getLow();

        for (CandleStick candleStick : candles) {
            BigDecimal compareLow = candleStick.getLow();
            if(compareLow.compareTo(low) < 0){
                low = compareLow;
            }
        }

        return low;
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



    public static CandleType getCandleType(CandleStick candleStick, BigDecimal shortGapRate, BigDecimal steadyGapRate){


        BigDecimal shortGap = candleStick.getOpen().multiply(shortGapRate);
        BigDecimal steadyGap = candleStick.getOpen().multiply(steadyGapRate);


        BigDecimal height = candleStick.getHeight();
        //길이가 보합세보다 작을때
//        if(height <= steadyGap){
        if(height.compareTo(steadyGap) <= 0){
            return CandleType.STEADY;
        }


        BigDecimal change = candleStick.getChange();
        BigDecimal absChange = change.abs();

        BigDecimal high = candleStick.getHigh();
        BigDecimal close = candleStick.getClose();
        BigDecimal open = candleStick.getOpen();
        BigDecimal low = candleStick.getLow();

        BigDecimal upperShadow;
        BigDecimal lowerShadow;

        final BigDecimal decimal_2 = new BigDecimal(2);

        if(change.compareTo(BigDecimal.ZERO) < 0){
            upperShadow = high.subtract(open);
            lowerShadow = close.subtract(low);
        }else{
            upperShadow = high.subtract(close);
            lowerShadow = open.subtract(low);
        }

        //위 그림자 캔들
        if(upperShadow.compareTo(lowerShadow) > 0
                //위그림자가 아래꼬리보다 크고
                && upperShadow.compareTo(absChange) > 0
                //위그림자가 몸통보다 크고
                && upperShadow.compareTo(steadyGap) > 0
            //위그림자가 보합갭 보다 크고
        ){
            if(lowerShadow.compareTo(steadyGap) < 0){
                //아래그림자가 보합갭보다 짧으면

                return CandleType.UPPER_SHADOW;
            }

            BigDecimal rate = upperShadow.divide(lowerShadow, MathContext.DECIMAL128 );
            if(rate.compareTo(decimal_2) >= 0){
                //위꼬리가 아래그림자보다 2배이상 길면
                return CandleType.UPPER_SHADOW;
            }
        }

        //아래그림자 캔들
        if(lowerShadow.compareTo(upperShadow) > 0
                //아래그림자가 위꼬리보다 크고
                && lowerShadow.compareTo(absChange) > 0
                //아래그림자가 몸통보다 크고
                && lowerShadow.compareTo(steadyGap) > 0
            //아래그림자가 보합보다 크고
        ){
            if(upperShadow.compareTo(steadyGap) < 0){
                //위그림자가 보합갭보다 짧으면
                return CandleType.LOWER_SHADOW;
            }

            BigDecimal rate = lowerShadow.divide(upperShadow, MathContext.DECIMAL128);
            if(rate.compareTo(decimal_2) >= 2.0){
                //아래그림자가 위꼬리보다 2배이상 길면
                return CandleType.LOWER_SHADOW;
            }

        }

        //위 아래 그림자캔들
        if(
                lowerShadow.compareTo(absChange) > 0
                        //아래그림자가 몸통보다 길고
                        && upperShadow.compareTo(absChange) > 0
                        //위그림자가 몸통보다 길고
                        && lowerShadow.compareTo(steadyGap) > 0
                        //아래그림자가 보합길이보다 길고
                        && upperShadow.compareTo(steadyGap) > 0
            // 위그림자가 보합길이보다 길고
        ){

            if(absChange.compareTo(steadyGap) < 0){
                //몸통이 보합걸이보다 작으면
                return CandleType.DOJI;
            }

            BigDecimal upperRate = upperShadow.divide(absChange, MathContext.DECIMAL128);
            BigDecimal lowerRate = lowerShadow.divide(absChange, MathContext.DECIMAL128);

            if(upperRate.compareTo(decimal_2) >= 0 && lowerRate.compareTo(decimal_2) >= 0){
                // 위아래꼬리가 몸통보다 많이길면 길면

                return CandleType.HIGH_WAVE;
            }else{
                // 위아래꼬리가 길면

                return CandleType.SPINNING_TOPS;
            }
        }

        //유형을 정하지 못하고 이 부분까지 온경우
        //긴캔들 짧은캔들
        if(absChange.compareTo(shortGap) <= 0){
            //몸통길이가 sortGap 짧으면 짧은캔들

            return CandleType.SHORT;
        }else{
            //몸통길이가 sortGap 길면 긴캔들

            return CandleType.LONG;
        }

    }

    public static String getMaxYmd(CandleStick [] candles, ZoneId zoneId){

        String maxYmd = YmdUtil.getYmd(candles[0].getOpenTime(), TradingTimes.KOR_ZONE_ID);

        for (int i = 1; i <candles.length ; i++) {
            String ymd = YmdUtil.getYmd(candles[i].getOpenTime(), TradingTimes.KOR_ZONE_ID);

            if(YmdUtil.compare(ymd, maxYmd)> 0){
                maxYmd = ymd;
            }
        }

        return maxYmd;
    }


    public static BigDecimal mdd(CandleStick [] candles){
        BigDecimal high = high(candles);
        BigDecimal low = low(candles);
        return TradingMath.mdd(high,low);
    }

}
