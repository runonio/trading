
package io.runon.trading.technical.analysis.candle;

import io.runon.trading.BigDecimals;
import io.runon.trading.Price;
import io.runon.trading.PriceChangeRate;
import io.runon.trading.technical.analysis.indicators.ma.Sma;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * candle 에서 사용하는 정보 일부를 BigDecimal 형으로 변환 시켜주는 유틸성 클래스
 *
 * @author macle
 */
public class CandleBigDecimals {

    public static BigDecimal [] getCloseArray(Price[] array){
        return getCloseArray(array,0, array.length);
    }

    public static BigDecimal [] getCloseArray(Price[] array, int resultLength){
        return getCloseArray(array, array.length- resultLength, array.length);

    }

    public static BigDecimal [] getCloseArray(Price[] array,int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        BigDecimal [] result = new BigDecimal[end-startIndex];
        int index = 0;
        for (int i = startIndex; i <end ; i++) {
            result[index++] = array[i].getClose() ;
        }
        return result;

    }

    public static BigDecimal[] getChangeRateArray(PriceChangeRate[] array){
        return getChangeRateArray(array, 0, array.length);
    }


    public static BigDecimal[] getChangeRateArray(PriceChangeRate[] array, int resultLength){
        return getChangeRateArray(array,array.length - resultLength, array.length);
    }


    public static BigDecimal[] getChangeRateArray(PriceChangeRate[] array, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }
        BigDecimal [] result = new BigDecimal[end-startIndex];
        int index = 0;
        for (int i = startIndex; i <end ; i++) {
            result[index++] = array[i].getChangeRate() ;
        }
        return result;
    }


    public static BigDecimal[] getAmountArray(TradeCandle[] array){
        return getAmountArray(array, 0 , array.length);
    }
    public static BigDecimal[] getAmountArray(TradeCandle[] array, int resultLength){
        return getAmountArray(array, array.length - resultLength, array.length);
    }

    public static BigDecimal[] getAmountArray(TradeCandle[] array, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        BigDecimal [] result = new BigDecimal[end-startIndex];
        int index = 0;
        for (int i = startIndex; i <end ; i++) {
            result[index++] = array[i].getAmount() ;
        }
        return result;
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
