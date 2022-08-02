
package io.runon.trading.technical.analysis.candle;

import io.runon.trading.BigDecimals;
import io.runon.trading.Price;
import io.runon.trading.PriceChangeRate;
import io.runon.trading.technical.analysis.indicator.ma.Sma;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * candle 에서 사용하는 정보 일부를 BigDecimal 형으로 변환 시켜주는 유틸성 클래스
 *
 * @author macle
 */
public class CandleBigDecimals {

    public static BigDecimal [] getCloseArray(Price[] candles){
        return getCloseArray(candles,0, candles.length);
    }

    public static BigDecimal [] getCloseArray(Price[] candles, int length){
        return getCloseArray(candles, candles.length- length, candles.length);

    }

    public static BigDecimal [] getCloseArray(Price[] candles,int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

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


    /**
     * 표준편차
     * 표준 편차(標準 偏差, 영어: standard deviation, SD)는 통계집단의 분산의 정도 또는 자료의 산포도를 나타내는 수치
     */
    public static BigDecimal sd( CandleStick [] array, BigDecimal avg , int n, int index){


        int end = index +1;
        int start = end -n;
        if(start < 0) {
            start = 0;
        }
        int length = end - start ;

        if(length < 1){
            return BigDecimal.ZERO;
        }

        if(avg == null){
            avg = Sma.get(array, n, index);
        }

        BigDecimal d = BigDecimal.ZERO;

        for (int i = start; i < end; i++) {
            d = d.add(array[i].getClose().subtract(avg).pow(2));
        }

        d = d.divide(new BigDecimal(length), MathContext.DECIMAL128);


        return d.sqrt(BigDecimals.MC_10);
    }
}
