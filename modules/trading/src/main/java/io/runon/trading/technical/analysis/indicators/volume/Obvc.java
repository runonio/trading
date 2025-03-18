package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;

/**
 * obv에 변화량 가중치를 준다
 * @author macle
 */
public class Obvc {


    public static BigDecimal get(TradeCandle candle) {

        BigDecimal change = candle.getChangePercent();

        if(change.compareTo(BigDecimal.ZERO) > 0) {

            change = change.add(BigDecimal.ONE);
            if(change.compareTo(BigDecimals.DECIMAL_30) > 0) {
                change = BigDecimals.DECIMAL_30;
            }

            return BigDecimal.valueOf(Math.log10(change.doubleValue())).multiply(candle.getVolume());
        }else if (change.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }else{

            change = change.abs();
            change = change.add(BigDecimal.ONE);
            if(change.compareTo(BigDecimals.DECIMAL_30) > 0) {
                change = BigDecimals.DECIMAL_30;
            }
            return BigDecimal.valueOf(Math.log10(change.doubleValue())).multiply(candle.getVolume()).multiply(BigDecimals.DECIMAL_M_1);
        }
    }


    public static TimeNumber [] getArray(TradeCandle [] array){
        return getArray(array, BigDecimal.ZERO, 0, array.length);
    }

    public static TimeNumber[] getArray(TradeCandle [] array, BigDecimal init, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        BigDecimal sum = init;

        TimeNumber[] result = new TimeNumber[resultLength];

        for (int i = 0; i <resultLength ; i++) {
            int index = startIndex + i;

            TimeNumberData timeNumber = new TimeNumberData();
            timeNumber.setTime(array[index].getTime());
            sum = sum.add(get(array[index]));
            timeNumber.setNumber(sum);
            result[i] = timeNumber;
        }

        return result;

    }

    public static void main(String[] args) {


    }
}
