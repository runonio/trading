package io.runon.trading.example.ma;

import io.runon.trading.technical.analysis.indicator.ma.Sma;

import java.math.BigDecimal;

/**
 * SMA 예제
 * @author macle
 */
public class SmaExample {
    public static void main(String[] args) {
        BigDecimal[] array = new BigDecimal[10];

        for (int i = 0; i < array.length; i++) {
            array[i] = new BigDecimal(i+1);
        }

        System.out.println(Sma.get(array,2).toPlainString());
        System.out.println(Sma.get(array,2, array.length-2).toPlainString());
        BigDecimal [] result = Sma.getArray(array,2,5);
//        BigDecimal [] result = Sma.getArray(array,2,3,5);
        System.out.println("array:");
        for(BigDecimal avg : result){
            System.out.println(avg.toPlainString());
        }


    }
}
