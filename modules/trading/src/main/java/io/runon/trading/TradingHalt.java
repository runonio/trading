package io.runon.trading;

import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;

/**
 * 거래정지 확인하기
 * @author macle
 */
public class TradingHalt {

    public static boolean isHalt(TradeCandle [] candles){
        if(candles == null || candles.length == 0){
            return true;
        }

        if(candles.length == 1){
            TradeCandle candle = candles[0];
            if(  candle.getVolume().compareTo(BigDecimal.ZERO) == 0 && candle.getAmount().compareTo(BigDecimal.ZERO) == 0){
                return true;
            }
        }

        int lastHaltCount = 0;

        int last = candles.length - 10;
        if(last < -1){
            last = -1;
        }

        for (int i = candles.length-1; i >last ; i--) {
            TradeCandle candle = candles[i];
            if(candle.getVolume().compareTo(BigDecimal.ZERO) == 0 && candle.getAmount().compareTo(BigDecimal.ZERO) == 0){
                lastHaltCount++;
            }else{
                break;
            }

        }

        if(lastHaltCount > 0 ){
            return true;
        }

        int searchSize = 50;

        int haltCount = 0;

        last = candles.length - searchSize;
        if(last < -1){
            last = -1;
        }

        BigDecimal maxChangePercent = BigDecimal.ZERO;

        for (int i = candles.length-1; i >last ; i--) {
            TradeCandle candle = candles[i];
            if(candle.getVolume().compareTo(BigDecimal.ZERO) == 0 && candle.getAmount().compareTo(BigDecimal.ZERO) == 0){
                haltCount++;
            }
            maxChangePercent = maxChangePercent.max(candle.getChangePercent().abs());
        }

        if(haltCount > 10){
            return true;
        }
        
        //30퍼이상 움직이면 상장폐지종목
        if(maxChangePercent.compareTo(BigDecimals.DECIMAL_30) > 0){
            return true;
        }



        return false;
    }

}
