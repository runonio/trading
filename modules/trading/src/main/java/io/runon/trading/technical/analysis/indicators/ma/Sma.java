package io.runon.trading.technical.analysis.indicators.ma;

import io.runon.trading.Price;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.CandleBigDecimals;

import java.math.BigDecimal;
import java.math.MathContext;
/**
 * 단순이동평균
 * simple moving average
 * @author macle
 */
public class Sma {



    /**
     * 이동평균값얻기
     * @param array 배열
     * @param n 평균을 구하기위한 개수 N
     * @return 이동평균
     */
    public static BigDecimal get(Price[] array, int n){

        int averageCount = Math.min(array.length, n);
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = array.length - averageCount; i < array.length; i++) {
            sum = sum.add(array[i].getClose());
        }
        return sum.divide(new BigDecimal(averageCount), MathContext.DECIMAL128);
    }

    public static BigDecimal get(Price[] array, int n, int index){
        BigDecimal sum = BigDecimal.ZERO;
        int end = index +1;
        int start = end -n;
        if(start < 0) {
            start = 0;
        }

        for (int i = start; i < end; i++) {
            sum = sum.add(array[i].getClose());
        }
        return sum.divide(new BigDecimal(end - start), MathContext.DECIMAL128);
    }




    /**
     * 이동평균값얻기
     * @param array 배열
     * @param n 평균을 구하기위한 개수 N
     * @return 이동평균
     */
    public static BigDecimal get(BigDecimal[] array, int n){
        int averageCount = Math.min(array.length, n);
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = array.length - averageCount; i < array.length; i++) {
            sum = sum.add(array[i]);
        }
        return sum.divide(new BigDecimal(averageCount), MathContext.DECIMAL128);
    }

    public static BigDecimal get(BigDecimal[] array, int n, int index){
        BigDecimal sum = BigDecimal.ZERO;
        int end = index +1;
        int start = end -n;
        if(start < 0) {
            start = 0;
        }

        for (int i = start; i < end; i++) {
            sum = sum.add(array[i]);
        }
        return sum.divide(new BigDecimal(end - start), MathContext.DECIMAL128);
    }


    /**
     * 평균 배열 얻기
     * @param array 배열
     * @param n 평균을 구하기위한 개수 N
     * @param resultLength 결과 배열 카운드 (얻고자 하는 수)
     * @return 평균 배열
     */
    public static BigDecimal[] getArray(Price[] array, int n, int resultLength) {
        return getArray(CandleBigDecimals.getCloseArray(array), n, array.length - resultLength, array.length);
    }

    /**
     * 평균 배열 얻기
     *
     * @param array      보통 종가 배열을 많이사용 함
     * @param n            평균을 구하기위한 개수 N
     * @param resultLength 결과 배열 카운드 (얻고자 하는 수)
     * @return 평균 배열
     */
    public static BigDecimal[] getArray(BigDecimal[] array, int n, int resultLength) {
       return getArray(array, n, array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, int n, int startIndex, int end) {
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


    public static TimeNumber getTimeNumber(TimeNumber [] array, int n){
        TimeNumberData timeNumber = new TimeNumberData();

        int startIndex = array.length- n;
        if(startIndex < 0){
            startIndex = 0;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = startIndex; i <array.length ; i++) {
            sum = sum.add(array[i].getNumber());
        }

        TimeNumberData sma = new TimeNumberData();
        sma.setTime(array[array.length-1].getTime());
        sma.setNumber(sum.divide(new BigDecimal(array.length-startIndex), MathContext.DECIMAL128));

        return sma;
    }

    public static TimeNumber[] getTimeNumbers(TimeNumber [] array, int n, int resultLength){

        return getTimeNumbers(array, n, array.length - resultLength, array.length);
    }

    public static TimeNumber[] getTimeNumbers(TimeNumber [] array, int n, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        if(resultLength < 1){
            return TimeNumber.EMPTY_ARRAY;
        }
        int gap = startIndex+1;

        TimeNumber [] averages = new TimeNumber[resultLength];
        for (int i = 0; i < resultLength; i++) {
            int endJ = gap + i;
            int start = endJ - n;
            int avgN = n;
            if (start < 0) {
                start = 0;
                avgN = endJ - start;
            }

            BigDecimal sum = BigDecimal.ZERO;
            for (int j = start; j < endJ; j++) {
                sum = sum.add(array[j].getNumber());
            }

            TimeNumberData timeNumber = new TimeNumberData();
            timeNumber.setTime(array[endJ-1].getTime());
            timeNumber.setNumber(sum.divide(new BigDecimal(avgN), MathContext.DECIMAL128));

            averages[i] = timeNumber;
        }

        return averages;
    }

}
