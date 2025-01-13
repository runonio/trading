package io.runon.trading;

import io.runon.trading.exception.TradingDataException;

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

}
