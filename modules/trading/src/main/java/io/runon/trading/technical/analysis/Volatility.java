package io.runon.trading.technical.analysis;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.TimeNumbers;
import io.runon.trading.TradingMath;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.indicators.Disparity;
import io.runon.trading.technical.analysis.indicators.ma.Sma;

import java.math.BigDecimal;

/**
 * 변동성 관련 유틸성 클래스
 * @author macle
 */
public class Volatility {


    public static TimeNumber [] smaPercent(CandleStick[] array, int n){

        return smaPercent(array, n, 0, array.length);
    }

    public static TimeNumber [] smaPercent(CandleStick[] array, int n, int beginIndex, int end){
        TimeNumber [] ema = new TimeNumber[array.length];
        for(int i = 0; i < ema.length; i++){
            TimeNumberData data = new TimeNumberData(array[i].getTime(), array[i].getVolatilityPercent());
            ema[i] = data;
        }
        return Sma.getTimeNumbers(ema, n, beginIndex, end);
    }

    public static TimeNumber [] smaDisparity(CandleStick[] array, int shortN, int longN){
        return smaDisparity(array, shortN, longN, 0, array.length);
    }

    public static TimeNumber [] smaDisparity(CandleStick[] array, int shortN, int longN, int beginIndex, int end){
        if(beginIndex < 0){
            beginIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - beginIndex;
        if(resultLength < 1){
            return TimeNumbers.EMPTY_ARRAY;
        }
        int gap = beginIndex+1;

        TimeNumber [] averages = new TimeNumber[resultLength];

        for (int i = 0; i < resultLength; i++) {
            int endJ = gap + i;


            int start = endJ - longN;
            if (start < 0) {
                start = 0;
            }

            BigDecimal [] longNumbers = new BigDecimal[endJ - start];
            int numIndex =0;
            for (int j = start; j < endJ; j++) {
                longNumbers[numIndex++] = HighLow.getVolatility(array[j]);
            }

            BigDecimal longAvg = TradingMath.average(longNumbers, new BigDecimal("0.1"));

            start = endJ - shortN;
            if (start < 0) {
                start = 0;
            }
            BigDecimal [] shortNumbers = new BigDecimal[endJ - start];
            numIndex =0;
            for (int j = start; j < endJ; j++) {
                shortNumbers[numIndex++] = HighLow.getVolatility(array[j]);
            }

            BigDecimal shortAvg = TradingMath.average(shortNumbers);
            averages[i] = new TimeNumberData(array[endJ-1].getTime(), Disparity.get(shortAvg, longAvg));
        }


        return averages;
//        return Sma.getTimeNumbers(ema, n, beginIndex, end);
    }


}
