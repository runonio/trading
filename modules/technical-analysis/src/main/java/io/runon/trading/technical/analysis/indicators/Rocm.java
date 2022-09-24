package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.CandleStick;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Price Rate of Change Middle
 * ROC 값을 중간값으로 계산하여 사용하기 위한연구공식
 * chuljoo.tistory.com/145
 *
 * ((고가+저가+종가)-(N일 고가 + 저가 + 종가))/3 / n일전종간가격 * 100
 *
 * @author macle
 */
public class Rocm  extends NIndicators<CandleStick> {

    @Override
    public BigDecimal get(CandleStick[] array, int n, int index) {

        if(index < 0){
            return BigDecimal.ZERO;
        }

        int pIndex = index-n;

        if(pIndex < 0){
            return BigDecimal.ZERO;
        }

        BigDecimal price =  array[index].getHighLowClose();
        BigDecimal previous = array[pIndex].getHighLowClose();

        BigDecimal middle = previous.divide(BigDecimals.DECIMAL_3, MathContext.DECIMAL128);

        return price.subtract(previous).divide(BigDecimals.DECIMAL_3, MathContext.DECIMAL128).multiply(BigDecimals.DECIMAL_100).divide(middle ,scale, RoundingMode.HALF_UP).stripTrailingZeros();
    }
}
