package io.runon.trading.technical.analysis.trend;

import io.runon.trading.BigDecimals;
import io.runon.trading.TimePrice;
import io.runon.trading.technical.analysis.indicators.Disparity;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author macle
 */
public class TrendDisparity {

    public static BigDecimal get(TimePrice [] shortArray, TimePrice [] longArray, long time){

        int gap = shortArray.length - longArray.length;
        long beginTime = shortArray[shortArray.length-1].getTime() - time;


        int cnt = 0;
        BigDecimal sum = BigDecimal.ZERO;

        for(int i = shortArray.length-1 ; i > -1 ; i--){
            int longIndex = i + gap;
            if(longIndex < 0){
                break;
            }

            if(longIndex > longArray.length-1){
                continue;
            }

            TimePrice tp = shortArray[i];

            if(tp.getTime() < beginTime){
                break;
            }

            cnt++;
            sum = sum.add(Disparity.get(shortArray[i].getClose(), longArray[longIndex].getClose()));
        }

        if(cnt == 0){
            return BigDecimals.DECIMAL_100;
        }

        return sum.divide(new BigDecimal(cnt), 2, RoundingMode.HALF_UP);
    }

}
