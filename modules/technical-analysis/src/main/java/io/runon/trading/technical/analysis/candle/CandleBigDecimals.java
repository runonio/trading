
package io.runon.trading.technical.analysis.candle;

import io.runon.trading.Price;
import io.runon.trading.PriceChangeRate;

import java.math.BigDecimal;

/**
 * candle 에서 사용하는 정보 일부를 BigDecimal 형으로 변환 시켜주는 유틸성 클래스
 *
 * @author macle
 */
public class CandleBigDecimals {

    public static BigDecimal [] getCloseArray(Price[] candles){
        return getCloseArray(candles,0, candles.length);
    }

    public static BigDecimal [] getCloseArray(Price[] candles,int startIndex, int end){
        BigDecimal [] array = new BigDecimal[end-startIndex];
        int index = 0;
        for (int i = startIndex; i <end ; i++) {
            array[index++] = candles[i].getClose() ;
        }
        return array;

    }

    public static BigDecimal[] getChangeRateArray(PriceChangeRate[] priceChangeRateArray){
        return getChangeRateArray(priceChangeRateArray, 0, priceChangeRateArray.length);
    }

    public static BigDecimal[] getChangeRateArray(PriceChangeRate[] priceChangeRateArray, int startIndex, int end){
        BigDecimal [] array = new BigDecimal[end-startIndex];
        int index = 0;
        for (int i = startIndex; i <end ; i++) {
            array[index++] = priceChangeRateArray[i].getChangeRate() ;
        }
        return array;
    }


    public static BigDecimal[] getTradingPriceArray(TradeCandle[] candles){
        return getTradingPriceArray(candles, 0 , candles.length);
    }

    public static BigDecimal[] getTradingPriceArray(TradeCandle[] candles, int startIndex, int end){
        BigDecimal [] array = new BigDecimal[end-startIndex];
        int index = 0;
        for (int i = startIndex; i <end ; i++) {
            array[index++] = candles[i].getTradingPrice() ;
        }
        return array;
    }
}
