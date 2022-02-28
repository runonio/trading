package io.runon.trading.technical.analysis.indicator;

import io.runon.trading.BigDecimals;
import io.runon.trading.Price;
import io.runon.trading.technical.analysis.indicator.ma.MovingAverage;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 이격도
 * @author macle
 */
public class Disparity {
    /**
     * 이격도 얻기
     */
    public static BigDecimal get(Price[] prices, int avgCount){

        return get(prices[prices.length-1], prices, avgCount);
    }

    /**
     * 이격도 얻기
     */
    public static BigDecimal get(Price price, Price[] prices, int avgCount){
        return price.getClose().divide(MovingAverage.getAverage(prices, avgCount) , 4, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
    }


    /**
     * 이격도 얻기
     */
    public static BigDecimal get(BigDecimal[] prices, int avgCount){
        return get(prices[prices.length-1], prices, avgCount).multiply(BigDecimals.DECIMAL_100);
    }

    /**
     * 이격도 얻기
     */
    public static BigDecimal get(BigDecimal price, BigDecimal[] prices, int avgCount){
        return price.divide(MovingAverage.getAverage(prices, avgCount) , 4, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
    }

    /**
     * 이격도 얻기
     */
    public static BigDecimal get(BigDecimal shortValue, BigDecimal longValue){
        return shortValue.divide( longValue, 4, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
    }




}
