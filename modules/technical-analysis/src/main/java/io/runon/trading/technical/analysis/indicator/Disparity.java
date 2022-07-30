package io.runon.trading.technical.analysis.indicator;

import io.runon.trading.BigDecimals;
import io.runon.trading.Price;
import io.runon.trading.technical.analysis.indicator.ma.Sma;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 이격도
 * @author macle
 */
public class Disparity {
    /**
     * SMA 와 이격도
     */
    public static BigDecimal getSmaDisparity(Price[] prices, int avgCount){

        return getSmaDisparity(prices[prices.length-1], prices, avgCount);
    }

    /**
     *  SMA 와 이격도
     */
    public static BigDecimal getSmaDisparity(Price price, Price[] prices, int avgCount){
        return price.getClose().divide(Sma.get(prices, avgCount) , 4, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
    }


    /**
     *  SMA 와 이격도
     */
    public static BigDecimal getSmaDisparity(BigDecimal[] prices, int avgCount){
        return getSmaDisparity(prices[prices.length-1], prices, avgCount).multiply(BigDecimals.DECIMAL_100);
    }

    /**
     *  SMA 와 이격도
     */
    public static BigDecimal getSmaDisparity(BigDecimal price, BigDecimal[] prices, int avgCount){
        return price.divide(Sma.get(prices, avgCount) , 4, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
    }

    /**
     * 이격도 얻기
     */
    public static BigDecimal get(BigDecimal shortValue, BigDecimal longValue){
        return get(shortValue, longValue, 4);
    }

    /**
     * 이격도 얻기
     */
    public static BigDecimal get(BigDecimal shortValue, BigDecimal longValue, int scale){
        return shortValue.divide( longValue, scale, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
    }

}
