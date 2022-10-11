package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.Time;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;

import java.math.BigDecimal;

/**
 * 기간 N 으로만 구현이 가능한 지표 추상체
 * @author macle
 */
public abstract class NTimeNumberIndicators<T extends Time> {

    protected int defaultN = 14;

    public void setDefaultN(int defaultN) {
        this.defaultN = defaultN;
    }

    protected int scale = 4;
    public void setScale(int scale) {
        this.scale = scale;
    }
    public abstract BigDecimal get(T [] array, int n , int index);

    public TimeNumber get(T [] array, int index){
        return new TimeNumberData( array[index].getTime(), get(array, defaultN, index));
    }

    public TimeNumber [] getArray(T [] array, int resultLength){
        return getArray(array, defaultN, array.length - resultLength, array.length);
    }

    public TimeNumber [] getArray(T [] array, int n, int resultLength){

        return getArray(array, n, array.length - resultLength, array.length);
    }

    public TimeNumber [] getArray(T [] array, int n, int startIndex, int end){
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

        TimeNumber [] data = new TimeNumber[resultLength];

        for (int i = 0; i < resultLength; i++) {
            int index = i + startIndex;

            data[i] = new TimeNumberData(array[index].getTime(), get(array, n, index));
        }

        return data;
    }
}
