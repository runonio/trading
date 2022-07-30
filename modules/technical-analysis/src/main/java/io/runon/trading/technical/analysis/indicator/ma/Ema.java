package io.runon.trading.technical.analysis.indicator.ma;

import io.runon.trading.BigDecimals;
import io.runon.trading.Price;
import io.runon.trading.TimeNumber;

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

//    public static BigDecimal[] getArray(TimeNumber[] closeArray, BigDecimal initPreviousEmaEma, int n, int averageCount) {
//
//    }

    public static BigDecimal multiplier(int n){
        // 2 / (1 + n)
        return BigDecimals.DECIMAL_2.divide(new BigDecimal(1+n), MathContext.DECIMAL128);
    }
}
