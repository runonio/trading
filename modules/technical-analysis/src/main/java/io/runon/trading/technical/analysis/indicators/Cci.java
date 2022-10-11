package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.technical.analysis.candle.CandleStick;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 *  Commodity Channel Index
 *  공식
 * CCI = ( M - m ) / ( 0.015 x d )
 * M : ( 고가 + 저가 + 종가 ) / 3
 * m : M의 일정기간 이동평균
 * d : M 과 m 사이 편차의 절대값을 단순평균한 값
 *
 * @author macle
 */
public class Cci extends NTimeNumberIndicators<CandleStick> {

    public static final BigDecimal D_MULTIPLY_DEFAULT = new BigDecimal("0.015");

    private BigDecimal dMultiply = D_MULTIPLY_DEFAULT;

    public void setDMultiply(BigDecimal dMultiply) {
        this.dMultiply = dMultiply;
    }

    public Cci(){
        defaultN = 14;
    }
    @Override
    public BigDecimal get(CandleStick[] array, int n, int index) {


        BigDecimal m = array[index].getMiddle();

        int end = index+1;
        int startIndex = end -n;
        if(end > array.length){
            end = array.length;
        }

        if(startIndex < 0){
            startIndex = 0;
        }

        int length = end - startIndex;
        if(length == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = startIndex; i <end ; i++) {
            sum = sum.add(array[i].getMiddle());
        }

        BigDecimal l = new BigDecimal(length);

        BigDecimal avg = sum.divide(l, MathContext.DECIMAL128);
        BigDecimal dSum = BigDecimal.ZERO;

        for (int i = startIndex; i <end ; i++) {
            dSum = dSum.add(array[i].getMiddle().subtract(avg).abs());
        }

        BigDecimal d = dSum.divide(l, MathContext.DECIMAL128);

        if(d.compareTo(BigDecimal.ZERO)== 0){
            return BigDecimal.ZERO;
        }

        BigDecimal up = m.subtract(avg);
        BigDecimal down = dMultiply.multiply(d);

        return up.divide(down,scale, RoundingMode.HALF_UP);
    }
}