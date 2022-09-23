package io.runon.trading.technical.analysis.indicators.fibonacci;

import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.hl.HighLowLeftSearch;

import java.math.BigDecimal;

/**
 * @author macle
 */
public class Fibonacci {

    public static final BigDecimal N236 = new BigDecimal("0.236");
    public static final BigDecimal N382 = new BigDecimal("0.382");
    public static final BigDecimal N500 = new BigDecimal("0.5");
    public static final BigDecimal N618 = new BigDecimal("0.618");

    public static FibonacciData get(BigDecimal high, BigDecimal low){
        BigDecimal height = high.subtract(low);
        FibonacciData data = new FibonacciData();
        data.n000 = low;
        data.n1000 = high;
        if(height.compareTo(BigDecimal.ZERO) == 0){
            data.n236 = low;
            data.n382 = low;
            data.n500 = low;
            data.n618 = low;
            return data;
        }

        data.n236 = low.add(N236.multiply(height));
        data.n382 = low.add(N382.multiply(height));
        data.n500 = low.add(N500.multiply(height));
        data.n618 = low.add(N618.multiply(height));
        return data;
    }

    public static FibonacciData supportLine(CandleStick[] array, int n){
        return supportLine(array, n, array.length-1);
    }

    /**
     * 
     * 저항선 피보나치
     * 반등장에서 사용
     */
    public static FibonacciData supportLine(CandleStick[] array, int n, int index){
        HighLow highLow = HighLowLeftSearch.getHighNextLow(array, n, index);
        FibonacciData fibonacciData = get(highLow.getHigh(), highLow.getLow());
        fibonacciData.time = highLow.getTime();
        fibonacciData.highTime = highLow.getHighTime();
        fibonacciData.lowTime = highLow.getLowTime();
        return fibonacciData;
    }

    public static FibonacciData resistanceLine(CandleStick[] array, int n){
        return resistanceLine(array, n, array.length-1);
    }

    /**
     * 지지선 피보나치
     * 조정장에서 사용
     */
    public static FibonacciData resistanceLine (CandleStick[] array, int n, int index){
        HighLow highLow = HighLowLeftSearch.getLowNextHigh(array, n, index);
        FibonacciData fibonacciData = get(highLow.getHigh(), highLow.getLow());
        fibonacciData.time = highLow.getTime();
        fibonacciData.highTime = highLow.getHighTime();
        fibonacciData.lowTime = highLow.getLowTime();
        return fibonacciData;
    }


    //벡테스팅용
    public static FibonacciData [] supportLines(CandleStick[] array, int n, int startIndex, int end) {
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        FibonacciData [] dataArray = new FibonacciData[resultLength];
        for (int i = 0; i < resultLength; i++) {
            dataArray[i] = supportLine(array,n, i+startIndex);
        }

        return dataArray;
    }

    public static FibonacciData [] resistanceLine(CandleStick[] array, int n, int startIndex, int end) {
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        FibonacciData [] dataArray = new FibonacciData[resultLength];
        for (int i = 0; i < resultLength; i++) {
            dataArray[i] = resistanceLine(array,n, i+startIndex);
        }

        return dataArray;
    }


}
