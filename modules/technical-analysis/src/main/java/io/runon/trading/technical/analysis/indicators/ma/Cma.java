package io.runon.trading.technical.analysis.indicators.ma;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Change Moving Average
 * 연구중 자료
 * 중간가격을 활용하여 평균 변화량을 얻는다 (변화율아님)
 * 보조지표와 가격의 기술기 차이를 계산하여 사용하기 위해 구현한다.
 * @author macle
 */
public class Cma {

    public static BigDecimal get(BigDecimal[] array, int n, int index) {

        int end = index +1;
        int start = end -n;
        if(start < 1) {
            start = 1;
        }

        int length = end - start;

        if(length < 1){
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = start; i < end; i++) {
            BigDecimal change = array[i].subtract(array[i-1]);
            sum = sum.add(change);
        }

        return sum.divide(new BigDecimal(length), MathContext.DECIMAL128);
    }



    public static BigDecimal get(TimeNumber[] array, int n, int index) {

        int end = index +1;
        int start = end -n;
        if(start < 1) {
            start = 1;
        }

        int length = end - start;

        if(length < 1){
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = start; i < end; i++) {
            BigDecimal change = array[i].getNumber().subtract(array[i-1].getNumber());
            sum = sum.add(change);
        }

        return sum.divide(new BigDecimal(length), MathContext.DECIMAL128);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, int n, int resultLength) {
        return getArray(array, n, array.length - resultLength, array.length);
    }

    public static BigDecimal[] getArray(BigDecimal[] array, int n, int startIndex, int end) {
        if(startIndex < 1){
            startIndex = 1;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        if(resultLength < 1){
            return new BigDecimal[0];
        }

        BigDecimal[] averages = new BigDecimal[resultLength];
        for (int i = 0; i < resultLength; i++) {
            averages[i] = get(array,n, startIndex+i);
        }
        return averages;
    }


    public static TimeNumber[] getTimeNumbers(TimeNumber [] array, int n, int resultLength){
        return getTimeNumbers(array, n, array.length - resultLength, array.length);
    }

    public static TimeNumber[] getTimeNumbers(TimeNumber [] array, int n, int startIndex, int end){
        if(startIndex < 1){
            startIndex = 1;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        if(resultLength < 1){
            return TimeNumber.EMPTY_ARRAY;
        }

        TimeNumber[] averages = new TimeNumber[resultLength];
        for (int i = 0; i < resultLength; i++) {
            int index = startIndex+i;
            averages[i] = new TimeNumberData(array[i].getTime(), get(array,n, index));
        }
        return averages;
    }
}
