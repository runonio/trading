package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.CandleStick;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
/**
 * (Relative Momentum Index)
 *  RSI 변형
 *
 *  공식
 * CCI = ( M - m ) / ( 0.015 x d )
 * M : ( 고가 + 저가 + 종가 ) / 3
 * m : M의 일정기간 이동평균
 * d : M 과 m 사이 편차의 절대값을 단순평균한 값
 *
 * @author macle
 */
public class Rmi  extends NTimeNumberIndicators<CandleStick> {

    private int defaultX = 5;

    public void setDefaultX(int defaultX) {
        this.defaultX = defaultX;
    }

    public Rmi(){
        defaultN = 20;
        scale = 2;
    }
    @Override
    public BigDecimal get(CandleStick[] array, int n, int index) {
        return get(array, n,defaultX, index).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public static BigDecimal get(CandleStick[] array, int n, int x,int index){
        int end = index+1;
        int startIndex = end -n;
        if(end > array.length){
            end = array.length;
        }

        if(startIndex - x < 0){
            return BigDecimals.DECIMAL_50;
        }

        int length = end - startIndex;
        if(length < 0){
            return BigDecimals.DECIMAL_50;
        }

        BigDecimal upSum = BigDecimal.ZERO;
        BigDecimal downSum = BigDecimal.ZERO;

        int upCount = 0;
        int downCount = 0;

        for (int i = startIndex; i < end; i++) {

            CandleStick candle = array[i];
            CandleStick previous = array[i-x];

            BigDecimal cr = candle.getClose().subtract(previous.getClose());

            if(cr.compareTo(BigDecimal.ZERO) > 0){
                upCount++;
                upSum = upSum.add(cr);
            }else if(cr.compareTo(BigDecimal.ZERO) < 0){
                downCount++;
                downSum = downSum.add(cr);
            }
        }

        return get(upCount, downCount, upSum, downSum);
    }

    public static BigDecimal get(int upCount, int downCount, BigDecimal upSum, BigDecimal downSum){
        if(upCount == 0 && downCount == 0){
            return BigDecimals.DECIMAL_50;
        }

        if(upCount == 0 ){
            return BigDecimal.ZERO;
        }
        if(downCount == 0){
            return BigDecimals.DECIMAL_100;
        }

        if(upSum.compareTo(BigDecimal.ZERO) == 0 && downSum.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimals.DECIMAL_50;
        }

        if(upSum.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }

        if(downSum.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimals.DECIMAL_100;
        }

        //  U = 전날 가격보다 오늘 상승할때의 상승폭 (up)
        //  D = 전달 가격보다 오늘 하락할때의 하락폭 (down)
        //  AU = 일정기간 (N) U의 평균값
        //  AD = 일정기간 (N) D의 평균값
        //  RS = AU / AD
        //  RSI =  RS / (1 + RS) =  AU / (AU + AD)
        BigDecimal au = upSum.divide(new BigDecimal(upCount), MathContext.DECIMAL128);
        //- 값이므로 -를 곲함 양수전환
        BigDecimal ad = downSum.multiply(BigDecimals.DECIMAL_M_1).divide(new BigDecimal(downCount),MathContext.DECIMAL128);

        BigDecimal rs = au.divide(ad, MathContext.DECIMAL128);
        BigDecimal rsi = rs.divide(BigDecimal.ONE.add(rs), MathContext.DECIMAL128);

        //소수점 4재짜리 까지만 사용하기
        //백분율 이기때문에  * 100의 효과
        return rsi.multiply(BigDecimals.DECIMAL_100);
    }

}
