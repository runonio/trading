package io.runon.trading.technical.analysis.indicator.ma;

import io.runon.trading.Price;
import io.runon.trading.technical.analysis.candle.CandleBigDecimals;

import java.math.BigDecimal;
import java.math.MathContext;
/**
 * 단순이동평균
 * simple moving average
 * @author macle
 */
public class Sma {

    /**
     * 이동평균값얻기
     * @param array 배열
     * @param n 평균을 구하기위한 개수 N
     * @return 이동평균
     */
    public static BigDecimal get(Price[] array, int n){

        int averageCount = Math.min(array.length, n);
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = array.length - averageCount; i < array.length; i++) {
            sum = sum.add(array[i].getClose());
        }
        return sum.divide(new BigDecimal(averageCount), MathContext.DECIMAL128);
    }

    /**
     * 이동평균값얻기
     * @param array 배열
     * @param n 평균을 구하기위한 개수 N
     * @return 이동평균
     */
    public static BigDecimal get(BigDecimal[] array, int n){
        int averageCount = Math.min(array.length, n);
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = array.length - averageCount; i < array.length; i++) {
            sum = sum.add(array[i]);
        }
        return sum.divide(new BigDecimal(averageCount), MathContext.DECIMAL128);
    }


    /**
     * 평균 배열 얻기
     * @param array 배열
     * @param n 평균을 구하기위한 개수 N
     * @param averageCount 평균 배열 카운드 (얻고자 하는 수)
     * @return 평균 배열
     */
    public static BigDecimal[] getArray(Price[] array, int n, int averageCount) {
        return getArray(CandleBigDecimals.getCloseArray(array), n, averageCount);
    }

    /**
     * 평균 배열 얻기
     *
     * @param array      보통 종가 배열을 많이사용 함
     * @param n            평균을 구하기위한 개수 N
     * @param averageCount 평균 배열 카운드 (얻고자 하는 수)
     * @return 평균 배열
     */
    public static BigDecimal[] getArray(BigDecimal[] array, int n, int averageCount) {

        if (averageCount > array.length) {
            averageCount = array.length;
        }

        BigDecimal[] averages = new BigDecimal[averageCount];

        //i를 포함해야 하기때문에 + 1을한다
        int arrayGap = array.length - averageCount + 1;

        for (int i = 0; i < averageCount; i++) {
            int end = arrayGap + i;
            int start = end - n;

            int length = n;
            if (start < 0) {
                start = 0;
                length = end;
            }
            BigDecimal sum = BigDecimal.ZERO;

            for (int j = start; j < end; j++) {
                sum = sum.add(array[j]);
            }
            averages[i] = sum.divide(new BigDecimal(length), MathContext.DECIMAL128);
        }
        return averages;
    }
}
