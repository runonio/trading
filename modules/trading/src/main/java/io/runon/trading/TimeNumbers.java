package io.runon.trading;

import io.runon.trading.exception.TradingDataException;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.Disparity;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 시간과 숫자
 * @author macle
 */
public class TimeNumbers {
    public static final TimeNumber[] EMPTY_ARRAY = new TimeNumber[0];

    public static <T extends Time> TimeNumber [] getTimeNumbers(T [] array, String filedName){

        try {
            if(array == null || array.length == 0){
                return EMPTY_ARRAY;
            }

            Field field = array[0].getClass().getDeclaredField(filedName);

            field.setAccessible(true);
            TimeNumber [] timeNumbers = new TimeNumber[array.length];
//
            for (int i = 0; i <timeNumbers.length ; i++) {
                T timeObj = array[i];
                TimeNumberData data = new TimeNumberData();
                data.setTime(timeObj.getTime());
                data.setNumber((BigDecimal) field.get(timeObj));
                timeNumbers[i] = data;
            }

            return timeNumbers;
        } catch (Exception e) {
            throw new TradingDataException(e);
        }
    }


    public static TimeNumber[] smaDisparity(TimeNumber[] array, int shortN, int longN){
        return smaDisparity(array, shortN, longN, 0, array.length);
    }

    public static TimeNumber [] smaDisparity(TimeNumber[] array, int shortN, int longN, int beginIndex, int end){
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
        int gap = beginIndex+1;

        TimeNumber [] averages = new TimeNumber[resultLength];

        for (int i = 0; i < resultLength; i++) {
            int endJ = gap + i;


            int start = endJ - longN;
            if (start < 0) {
                start = 0;
            }

            BigDecimal [] longNumbers = new BigDecimal[endJ - start];
            int numIndex =0;
            for (int j = start; j < endJ; j++) {
                longNumbers[numIndex++] = array[j].getNumber();
            }

            BigDecimal longAvg = TradingMath.average(longNumbers, new BigDecimal("0.1"));

            start = endJ - shortN;
            if (start < 0) {
                start = 0;
            }
            BigDecimal [] shortNumbers = new BigDecimal[endJ - start];
            numIndex =0;
            for (int j = start; j < endJ; j++) {
                shortNumbers[numIndex++] = array[j].getNumber();
            }

            BigDecimal shortAvg = TradingMath.average(shortNumbers);
            averages[i] = new TimeNumberData(array[endJ-1].getTime(), Disparity.get(shortAvg, longAvg));
        }


        return averages;
//        return Sma.getTimeNumbers(ema, n, beginIndex, end);
    }


}
