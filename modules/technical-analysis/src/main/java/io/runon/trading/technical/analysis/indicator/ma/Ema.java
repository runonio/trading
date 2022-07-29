package io.runon.trading.technical.analysis.indicator.ma;

import io.runon.trading.Price;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 지수이동평균
 * Exponential Moving Average
 * @author macle
 */
public class Ema {



    public static BigDecimal get(Price[] array, BigDecimal previousEma, int n){

        int averageCount = Math.min(array.length, n);
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = array.length - averageCount; i < array.length; i++) {
            sum = sum.add(array[i].getClose());
        }
        return sum.divide(new BigDecimal(averageCount), MathContext.DECIMAL128);
    }
}
