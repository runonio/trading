package io.runon.trading.technical.analysis.indicators.ma;

import io.runon.trading.*;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 지수이동평균
 * Exponential Moving Average
 * 지수이동평균은 가중변수를 이용하여 최근 수치의 영향력은 높이고 과거 수치의 영향력은 낮추는 것입니다. 이것은 이동평균이 가격변화를 보다 즉각적으로 반영하도록 하고 평균선의 움직임을 평활하게 해 줍니다.
 *
 * @author macle
 */
public class Ema {

    public static BigDecimal multiplier(int n){
        // 2 / (1 + n)
        return BigDecimals.DECIMAL_2.divide(new BigDecimal(1+n), MathContext.DECIMAL128);
    }
    public static BigDecimal get(Price close, Price previousEma,  int n){
        return get(close.getClose(), previousEma.getClose(), multiplier(n));
    }

    public static BigDecimal get(Price close, Price previousEma, BigDecimal multiplier){
        return get(close.getClose(), previousEma.getClose(), multiplier);
    }

    public static BigDecimal get(BigDecimal close, BigDecimal previousEma, int n){
        return get(close, previousEma, multiplier(n));
    }

    public static BigDecimal get(BigDecimal close, BigDecimal previousEma, BigDecimal multiplier){
        //공식 (금일 종가 * 승수) + (전일 EMA * (1 - 승수))
        //금일 종가 * 승수
        BigDecimal ema = close.multiply(multiplier);
        //전일 EMA * (1- 승수)
        BigDecimal add = previousEma.multiply(BigDecimal.ONE.subtract(multiplier));
        return ema.add(add);
    }

    public static BigDecimal[] getArray(Price[] array, int n, int resultLength) {
        return getArray(array, n, array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(Price[] array, int n, int startIndex, int end) {
        int initIndex = startIndex -1;

        BigDecimal initEma;
        if(initIndex < 1){
            initEma = array[0].getClose();
        }else{
            initEma = Sma.get(array, n, initIndex);
        }

        return getArray(array, initEma, multiplier(n), startIndex, end);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, int n, int resultLength) {
        return getArray(array, n, array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, int n, int startIndex, int end) {

        int initIndex = startIndex -1;

        BigDecimal initEma;
        if(initIndex < 1){
            initEma = array[0];
        }else{
            initEma = Sma.get(array, n, initIndex);
        }

        return getArray(array, initEma, multiplier(n), startIndex, end);

    }


    public static BigDecimal[] getArray(Price[] array, BigDecimal initPreviousEma, int n, int resultLength) {
        return getArray(array, initPreviousEma, multiplier(n), array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(Price[] array, BigDecimal initPreviousEma, BigDecimal multiplier, int resultLength) {
        return getArray(array, initPreviousEma, multiplier, array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, BigDecimal initPreviousEma, int n, int resultLength) {
        return getArray(array, initPreviousEma, multiplier(n), array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, BigDecimal initPreviousEma, BigDecimal multiplier, int resultLength) {
        return getArray(array,initPreviousEma,multiplier, array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, BigDecimal initPreviousEma, int n, int startIndex, int end) {
        return getArray(array,initPreviousEma, multiplier(n), startIndex, end);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, BigDecimal initPreviousEma, BigDecimal multiplier, int startIndex, int end) {
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal[] averages = new BigDecimal[resultLength];

        BigDecimal previousEma = initPreviousEma;

        for (int i = 0; i < resultLength; i++) {
            BigDecimal ema = get(array[i+startIndex], previousEma, multiplier);
            averages[i] = ema;
            previousEma = ema;
        }
        return averages;
    }
    public static BigDecimal[] getArray(Price[] array, BigDecimal initPreviousEma, BigDecimal multiplier, int startIndex, int end) {
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal[] averages = new BigDecimal[resultLength];

        BigDecimal previousEma = initPreviousEma;

        for (int i = 0; i < resultLength; i++) {
            BigDecimal ema = get(array[i+startIndex].getClose(), previousEma, multiplier);
            averages[i] = ema;
            previousEma = ema;
        }
        return averages;
    }


    public static TimeNumber[] getTimeNumbers(TimeNumber[] array, BigDecimal initPreviousEma, int n, int resultLength) {
        return getTimeNumbers(array, initPreviousEma, multiplier(n), array.length - resultLength, array.length);
    }

    public static TimeNumber[] getTimeNumbers(TimeNumber[] array, BigDecimal initPreviousEma, BigDecimal multiplier, int resultLength) {
        return getTimeNumbers(array, initPreviousEma, multiplier,array.length - resultLength, array.length);
    }
    public static TimeNumber[] getTimeNumbers(TimeNumber[] array, int n) {
        return getTimeNumbers(array,n,array.length);
    }
    public static TimeNumber[] getTimeNumbers(TimeNumber[] array, int n, int resultLength) {
        int startIndex = array.length - resultLength;

        int initIndex = startIndex -1;

        BigDecimal initEma;
        if(initIndex < 1){
            initEma = array[0].getNumber();
        }else{

            BigDecimal sum = BigDecimal.ZERO;
            int end = initIndex +1;
            int start = end -n;
            if(start < 0) {
                start = 0;
            }

            for (int i = start; i < end; i++) {
                sum = sum.add(array[i].getNumber());
            }

            initEma = sum.divide(new BigDecimal(end - start), MathContext.DECIMAL128);
        }

        return getTimeNumbers(array, initEma, multiplier(n), array.length - resultLength, array.length);
    }

    public static TimeNumber[] getTimeNumbers(TimeNumber[] array, BigDecimal initPreviousEma, BigDecimal multiplier, int beginIndex, int end) {
        if(beginIndex < 0){
            beginIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - beginIndex;
        if(resultLength < 1){
            return TimeNumbers.EMPTY_ARRAY;
        }
        TimeNumber[] averages = new TimeNumber[resultLength];

        BigDecimal previousEma = initPreviousEma;

        for (int i = 0; i < resultLength; i++) {
            int arrayIndex = i+beginIndex;
            BigDecimal ema = get(array[arrayIndex].getNumber(), previousEma, multiplier);
            averages[i] = new TimeNumberData(array[arrayIndex].getTime(), ema);
            previousEma = ema;
        }
        return averages;

    }

}
