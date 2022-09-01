package io.runon.trading.example.ma;

import io.runon.trading.technical.analysis.indicators.ma.Ema;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * EMA 예제
 * @author macle
 */
public class EmaExample {
    public static void main(String[] args) {

        System.out.println("승수: " + Ema.multiplier(14).setScale(4, RoundingMode.HALF_UP).toPlainString());

        BigDecimal previousEma = new BigDecimal("46.6");
        BigDecimal close = new BigDecimal("46.75");
        System.out.println(Ema.get(close,previousEma,14).setScale(4,RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());


        BigDecimal[] array = new BigDecimal[100];

        for (int i = 0; i < array.length; i++) {
            array[i] = new BigDecimal(i+1);
        }

        BigDecimal [] result = Ema.getArray(array,14,100);
        for(BigDecimal avg : result){
            System.out.println(avg.setScale(4,RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        }
    }

}
