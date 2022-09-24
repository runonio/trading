package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.CandleStick;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *  Price Rate of Change
 * chuljoo.tistory.com/145
 *
 * (당일종가 - n일전종가 ) / n일전종가 * 100
 *
 * @author macle
 */
public class Roc extends NIndicators<CandleStick> {

    @Override
    public BigDecimal get(CandleStick[] array, int n, int index) {

        if(index < 0){
            return BigDecimal.ZERO;
        }

        int pIndex = index-n;

        if(pIndex < 0){
            return BigDecimal.ZERO;
        }
        BigDecimal price =  array[index].getClose();
        BigDecimal previous = array[pIndex].getClose();
        return price.subtract(previous).multiply(BigDecimals.DECIMAL_100).divide(previous,scale, RoundingMode.HALF_UP);
    }
}
