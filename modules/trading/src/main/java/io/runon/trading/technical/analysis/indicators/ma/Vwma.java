package io.runon.trading.technical.analysis.indicators.ma;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.TimeNumbers;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 거래량 가중 이동 평균선
 * 잘 설명해 놓은 블로그
 * academy.binance.com/ko/articles/volume-weighted-average-price-vwap-explained
 *
 * 평균값 및에 시세가 존재 한다면 일반적으로 거래량 대비 저평가 되어있어서 롱포지션을 잡는경우
 * 평균값 위에 시세가 존재 한다면 하락관점으로 숏포지션을 잡는경우
 *
 * 단기적인 관점에서 더 유용함
 *
 * 거래량 가중 평균가 = ∑ (대표 가격 * 거래량 ) / ∑ 거래량
 * 거래량 가중 평균가 = ∑ (거래대금) / ∑ 거래량
 * @author macle
 */
public class Vwma {
    public static BigDecimal get(TradeCandle[] array, int n){

        int averageCount = Math.min(array.length, n);
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal vSum = BigDecimal.ZERO;
        for (int i = array.length - averageCount; i < array.length; i++) {
            sum = sum.add(array[i].getAmount());
            vSum = vSum.add(array[i].getVolume());
        }

        return sum.divide(vSum, MathContext.DECIMAL128);
    }

    public static BigDecimal get(TradeCandle[] array, int n, int index){
        int end = index +1;
        int start = end -n;
        if(start < 0) {
            start = 0;
        }
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal vSum = BigDecimal.ZERO;
        for (int i = start; i < end; i++) {
            sum = sum.add(array[i].getAmount());
            vSum = vSum.add(array[i].getVolume());
        }
        return sum.divide(vSum, MathContext.DECIMAL128);
    }


    /**
     * 평균 배열 얻기
     *
     * @param array      보통 종가 배열을 많이사용 함
     * @param n            평균을 구하기위한 개수 N
     * @param resultLength 결과 배열 카운드 (얻고자 하는 수)
     * @return 평균 배열
     */
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

        BigDecimal[] averages = new BigDecimal[resultLength];
        for (int i = 0; i < resultLength; i++) {
            averages[i] = get(array,n, startIndex+i);
        }
        return averages;
    }

    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, int n){
        return getTimeNumbers(array, n, 0, array.length);
    }


    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, int n, int resultLength){
        return getTimeNumbers(array, n, array.length - resultLength, array.length);
    }

    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, int n, int startIndex, int end){
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
        TimeNumber[] averages = new TimeNumber[resultLength];
        for (int i = 0; i < resultLength; i++) {
            int arrayIndex = startIndex+i;
            averages[i] = new TimeNumberData(array[arrayIndex].getTime(), get(array,n, arrayIndex));
        }
        return averages;

    }

}
