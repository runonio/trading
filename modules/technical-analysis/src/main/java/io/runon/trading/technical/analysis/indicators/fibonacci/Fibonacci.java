package io.runon.trading.technical.analysis.indicators.fibonacci;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;

import java.math.BigDecimal;

/**
 * @author macle
 */
public class Fibonacci {

    public static FibonacciData [] EMPTY_ARRAY = new FibonacciData[0];

    public static final BigDecimal N236 = new BigDecimal("0.236");
    public static final BigDecimal N382 = new BigDecimal("0.382");
    public static final BigDecimal N500 = new BigDecimal("0.5");
    public static final BigDecimal N618 = new BigDecimal("0.618");

    public static final BigDecimal N764 = new BigDecimal("0.764");
  
    //저항선
    public static FibonacciData resistanceLine(BigDecimal high, BigDecimal low){


        BigDecimal height = high.subtract(low);
        FibonacciData data = new FibonacciData();

        data.n000 = low;
        data.n1000 = high;
        if(height.compareTo(BigDecimal.ZERO) == 0){
            data.n236 = low;
            data.n382 = low;
            data.n500 = low;
            data.n618 = low;
            data.n764 = low;
            return data;
        }

        data.n236 = low.add(N236.multiply(height));
        data.n382 = low.add(N382.multiply(height));
        data.n500 = low.add(N500.multiply(height));
        data.n618 = low.add(N618.multiply(height));
        data.n764 = low.add(N764.multiply(height));
        return data;
    }

    // 지지선
    public static FibonacciData supportLine(BigDecimal high, BigDecimal low){

        BigDecimal height = high.subtract(low);
        FibonacciData data = new FibonacciData();

        data.n000 = high;
        data.n1000 = low;
        if(height.compareTo(BigDecimal.ZERO) == 0){
            data.n236 = high;
            data.n382 = high;
            data.n500 = high;
            data.n618 = high;
            data.n764 = high;
            return data;
        }

        data.n236 = high.subtract(N236.multiply(height));
        data.n382 = high.subtract(N382.multiply(height));
        data.n500 = high.subtract(N500.multiply(height));
        data.n618 = high.subtract(N618.multiply(height));
        data.n764 = high.subtract(N764.multiply(height));
        return data;
    }

    public static FibonacciData supportLine(CandleStick[] array, int initN, int continueN){
        return supportLine(array, initN, continueN, array.length-1);
    }

    /**
     * 
     * 지지선 피보나치
     * 반등장에서 사용
     */
    public static FibonacciData supportLine(CandleStick[] array, int initN, int continueN, int index){
        HighLow highLow = HighLowCandleLeftSearch.getLowNextHigh(array, initN, continueN, index);
        FibonacciData fibonacciData = supportLine(highLow.getHigh(), highLow.getLow());
        fibonacciData.time = highLow.getTime();
        fibonacciData.highTime = highLow.getHighTime();
        fibonacciData.lowTime = highLow.getLowTime();
        return fibonacciData;
    }

    public static FibonacciData resistanceLine(CandleStick[] array, int initN, int continueN){
        return resistanceLine(array, initN, continueN, array.length-1);
    }

    /**
     * 저항선 피보나치
     * 조정장에서 사용
     */
    public static FibonacciData resistanceLine (CandleStick[] array, int initN, int continueN, int index){
        HighLow highLow = HighLowCandleLeftSearch.getHighNextLow(array, initN,continueN, index);
        FibonacciData fibonacciData = resistanceLine(highLow.getHigh(), highLow.getLow());
        fibonacciData.time = highLow.getTime();
        fibonacciData.highTime = highLow.getHighTime();
        fibonacciData.lowTime = highLow.getLowTime();
        return fibonacciData;
    }
    //벡테스팅용
    public static FibonacciData [] supportLines(CandleStick[] array, int initN, int continueN, int resultLength) {
        return supportLines(array,initN, continueN , array.length - resultLength, array.length);
    }

    //벡테스팅용
    public static FibonacciData [] supportLines(CandleStick[] array, int initN, int continueN, int startIndex, int end) {
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        if(resultLength < 1){
            return EMPTY_ARRAY;
        }


        FibonacciData [] dataArray = new FibonacciData[resultLength];
        for (int i = 0; i < resultLength; i++) {
            dataArray[i] = supportLine(array,initN, continueN, i+startIndex);
        }

        return dataArray;
    }

    public static FibonacciData [] resistanceLines(CandleStick[] array, int initN, int continueN, int resultLength) {
        return resistanceLines(array, initN, continueN , array.length - resultLength, array.length);
    }

    public static FibonacciData [] resistanceLines(CandleStick[] array, int initN, int continueN, int startIndex, int end) {
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        FibonacciData [] dataArray = new FibonacciData[resultLength];
        for (int i = 0; i < resultLength; i++) {
            dataArray[i] = resistanceLine(array,initN, continueN, i+startIndex);
        }

        return dataArray;
    }

    public static TimeNumber [] get000Array(FibonacciData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            FibonacciData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.n000);
        }

        return timeNumbers;
    }


    public static TimeNumber [] get236Array(FibonacciData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            FibonacciData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.n236);
        }

        return timeNumbers;
    }

    public static TimeNumber [] get382Array(FibonacciData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            FibonacciData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.n382);
        }

        return timeNumbers;
    }

    public static TimeNumber [] get500Array(FibonacciData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            FibonacciData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.n500);
        }

        return timeNumbers;
    }

    public static TimeNumber [] get618Array(FibonacciData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            FibonacciData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.n618);
        }

        return timeNumbers;
    }

    public static TimeNumber [] get764Array(FibonacciData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            FibonacciData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.n764);
        }

        return timeNumbers;
    }


    public static TimeNumber [] get1000Array(FibonacciData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            FibonacciData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.n1000);
        }

        return timeNumbers;
    }
}
