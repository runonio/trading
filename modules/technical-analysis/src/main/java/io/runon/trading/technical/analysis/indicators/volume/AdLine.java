package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 매집 분산 지표
 * Accumulation Distribution Line
 * chuljoo.tistory.com/136
 *
 * 예전책에는  ((종가 - 저가) - (고가 - 종가)) 부분이 종가 - 시가로 되어있는데 이방법 말고 최근 자료로 구현한다.
 * ((종가 - 저가) - (고가 - 종가)) / (고가 - 저가) * 거래량 + 전일 AD
 * @author macle
 */
public class AdLine {
    public static BigDecimal get(TradeCandle candle, BigDecimal previousAd){
        BigDecimal cl = candle.getClose().subtract(candle.getLow());
        BigDecimal hc = candle.getHigh().subtract(candle.getClose());
        BigDecimal numerator = cl.subtract(hc);
        BigDecimal denominator = candle.getHigh().subtract(candle.getLow());
        return numerator.multiply(candle.getVolume()).divide(denominator, MathContext.DECIMAL128).add(previousAd);
    }

    public static BigDecimal [] getArray(TradeCandle [] array,  int resultLength){
        return getArray(array, BigDecimal.ZERO, array.length - resultLength, array.length);
    }

    public static BigDecimal [] getArray(TradeCandle [] array, BigDecimal initAd, int resultLength){
        return getArray(array, initAd, array.length - resultLength, array.length);
    }

    public static BigDecimal [] getArray(TradeCandle [] array, BigDecimal initAd, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal previousAd = initAd;
        BigDecimal[] adArray = new BigDecimal[resultLength];
        for (int i = 0; i < resultLength; i++) {
            BigDecimal ad = get(array[i+startIndex], previousAd);
            adArray[i] = ad;
            previousAd = ad;
        }

        return adArray;
    }

    public static TimeNumber [] getTimeNumbers(TradeCandle [] array,  int resultLength){
        return getTimeNumbers(array, BigDecimal.ZERO, array.length - resultLength, array.length);
    }


    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, BigDecimal initAd, int resultLength){
        return getTimeNumbers(array, initAd, array.length - resultLength, array.length);
    }
    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, BigDecimal initAd, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal previousAd = initAd;
        TimeNumber[] adArray = new TimeNumber[resultLength];
        for (int i = 0; i < resultLength; i++) {
            int arrayIndex = i+startIndex;

            BigDecimal ad = get(array[arrayIndex], previousAd);
            adArray[i] = new TimeNumberData(array[arrayIndex].getTime(), ad);
            previousAd = ad;
        }

        return adArray;
    }
}
