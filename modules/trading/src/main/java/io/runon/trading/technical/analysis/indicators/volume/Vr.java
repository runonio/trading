package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.commons.config.Config;
import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.TimeNumbers;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Volume Ratio
 *  ( 주가상승일 거래량 / 주가하락일 거래량) * 100%
 *
 *  m.blog.naver.com/myungli/221964994908
 * @author macle
 */
public class Vr {

    public static final BigDecimal MAX_VOLUME_RATIO = new BigDecimal(Config.getConfig("max.volume.ratio", "500"));

    public static final int DEFAULT_N = 25;

    public static BigDecimal get(TradeCandle[] array, int n){
        return get(array, n, array.length-1);
    }

    public static BigDecimal get(TradeCandle[] array, int n, int index){
        BigDecimal sum = BigDecimal.ZERO;
        int end = index +1;
        int start = end - n;
        if(start < 0) {
            start = 0;
        }

        BigDecimal up = BigDecimal.ZERO;
        BigDecimal down = BigDecimal.ZERO;

        for (int i = start; i < end; i++) {
            BigDecimal change = array[i].getChange();
            if(change == null){
                change = array[i].getChangeRate();

                if(change == null){
                    continue;
                }
            }
            if(change.compareTo(BigDecimal.ZERO) > 0){
                up = up.add( array[i].getVolume());
            }
            if(change.compareTo(BigDecimal.ZERO) < 0){
                down = down.add( array[i].getVolume());
            }
        }
        if(down.compareTo(BigDecimal.ZERO) == 0){
            return MAX_VOLUME_RATIO;
        }

        BigDecimal vr = BigDecimals.DECIMAL_100.multiply(up).divide(down, MathContext.DECIMAL128);
        if(vr.compareTo(MAX_VOLUME_RATIO) > 0){
            return MAX_VOLUME_RATIO;
        }

        return vr;
    }

    public static BigDecimal[] getArray(TradeCandle[] array, int resultLength) {
        return getArray(array, DEFAULT_N, array.length - resultLength, array.length);
    }
    public static BigDecimal[] getArray(TradeCandle[] array, int n, int resultLength) {
        return getArray(array, n, array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(TradeCandle[] array, int n, int startIndex, int end) {
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal[] vrArray = new BigDecimal[resultLength];
        for (int i = 0; i < resultLength; i++) {
            vrArray[i] = get(array,n, startIndex+i);
        }
        return vrArray;
    }

    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, int resultLength){
        return getTimeNumbers(array, DEFAULT_N, array.length - resultLength, array.length);
    }

    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, int n, int resultLength){
        return getTimeNumbers(array, n, array.length - resultLength, array.length);
    }

    public static TimeNumber[] getTimeNumbers(TradeCandle[] array, int n, int startIndex, int end) {
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        if(resultLength < 1){
            return TimeNumbers.EMPTY_ARRAY;
        }

        TimeNumber[] vrArray = new TimeNumber[resultLength];
        for (int i = 0; i < resultLength; i++) {
            int arrayIndex = i+startIndex;
            vrArray[i] = new TimeNumberData(array[arrayIndex].getTime(), get(array,n, startIndex+i));
        }

        return vrArray;
    }

}
