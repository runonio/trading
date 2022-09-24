package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.CandleStick;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 당일중간가 / n일전중간가 * 100
 *
 * @author macle
 */
public class MomentumMiddle extends NIndicators<CandleStick> {

    @Override
    public BigDecimal get(CandleStick[] array, int n, int index) {

        if(index < 0){
            return BigDecimal.ZERO;
        }

        int pIndex = index-n;

        if(pIndex < 0){
            return BigDecimal.ZERO;
        }
        BigDecimal price =  array[index].getMiddle();
        BigDecimal previous = array[pIndex].getMiddle();
        return price.multiply(BigDecimals.DECIMAL_100).divide(previous,scale, RoundingMode.HALF_UP);
    }
}
