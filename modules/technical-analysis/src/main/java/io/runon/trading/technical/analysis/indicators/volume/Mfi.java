package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.NTimeNumberIndicators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *  MFI (Money Flow Index)
 * @author macle
 */
public class Mfi extends NTimeNumberIndicators<TradeCandle> {

    public Mfi(){
        defaultN = 14;
        scale = 2;
    }
    @Override
    public BigDecimal get(TradeCandle[] array, int n, int index) {


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
            return BigDecimals.DECIMAL_50;
        }

        BigDecimal upSum = BigDecimal.ZERO;
        BigDecimal downSum = BigDecimal.ZERO;

        for (int i = startIndex; i < end; i++) {
            TradeCandle tradeCandle = array[i];

            BigDecimal cr = tradeCandle.getChange();

            if(cr.compareTo(BigDecimal.ZERO) > 0){
                upSum = upSum.add(tradeCandle.getMiddle().multiply(tradeCandle.getVolume()));
            }else if(cr.compareTo(BigDecimal.ZERO) < 0){
                downSum = downSum.add(tradeCandle.getMiddle().multiply(tradeCandle.getVolume()));
            }
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

        BigDecimal mr = upSum.divide(downSum, MathContext.DECIMAL128);

        BigDecimal mfi = mr.divide(BigDecimal.ONE.add(mr), MathContext.DECIMAL128);


        return mfi.multiply(BigDecimals.DECIMAL_100).setScale(scale,RoundingMode.HALF_UP);
    }
}