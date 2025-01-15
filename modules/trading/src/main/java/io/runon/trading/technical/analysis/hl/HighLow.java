package io.runon.trading.technical.analysis.hl;

import io.runon.trading.BigDecimals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author macle
 */
public interface HighLow {
    BigDecimal getHigh();
    BigDecimal getLow();


    /**
     * 변동성 얻기
     * @return 변동성
     */
    static BigDecimal getVolatility(HighLow highLow){

        BigDecimal gap = highLow.getHigh().subtract(highLow.getLow());
        BigDecimal max = highLow.getHigh().abs().max(highLow.getLow().abs());
        return gap.divide(max, MathContext.DECIMAL128);
    }
    /**
     * 변동성 얻기
     * percent 가 붙으면 *100
     * @return 변동성
     */
    static BigDecimal getVolatilityPercent(HighLow highLow){
        return getVolatility(highLow).multiply(BigDecimals.DECIMAL_100).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
    }

}
