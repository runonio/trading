package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.indicators.ma.Ema;

import java.math.BigDecimal;

/**
 * 지수 이동평균의 차이를 구하여 오실레이털르 사용하는 경우가 많다
 * 그럴때 공통으로 활용하는 메소드
 * @author macle
 */
public class EmaOscillator {

    public static TimeNumber[] get(TimeNumber[] array, int shortN, int longN){
        TimeNumber [] sArray = Ema.getTimeNumbers(array, shortN);
        TimeNumber [] lArray = Ema.getTimeNumbers(array, longN);

        TimeNumber [] oArray = new TimeNumber[array.length];
        for (int i = 0; i <array.length ; i++) {
            BigDecimal n = sArray[i].getNumber().subtract(lArray[i].getNumber());
            oArray[i] = new TimeNumberData(array[i].getTime(), n);
        }
        return oArray;
    }
}
