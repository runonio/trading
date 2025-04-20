package io.runon.trading.technical.analysis.hl;

import io.runon.commons.data.IndexDecimal;
import io.runon.trading.BigDecimals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author macle
 */
public interface HighLow {
    BigDecimal getHigh();
    BigDecimal getLow();


    /**
     * 변동성 얻기
     * @return 변동성
     */
    static BigDecimal getVolatility(HighLow highLow){

        BigDecimal gap = highLow.getHigh().subtract(highLow.getLow());
        BigDecimal max = highLow.getHigh().abs().max(highLow.getLow().abs());
        return gap.divide(max, MathContext.DECIMAL128);
    }
    /**
     * 변동성 얻기
     * percent 가 붙으면 *100
     * @return 변동성
     */
    static BigDecimal getVolatilityPercent(HighLow highLow){
        return getVolatility(highLow).multiply(BigDecimals.DECIMAL_100).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    static boolean isLow(HighLow[] array, int index, int distance){

        HighLow highLow = array[index];

        int left = index - distance;
        if(left < 0){
            left = 0;
        }
        for (int i = left; i <index ; i++) {
            if(highLow.getLow().compareTo(array[i].getLow()) > 0){
                return false;
            }
        }

        int right = index + distance;
        if(right > array.length){
            right = array.length;
        }

        for (int i = index +1; i <right ; i++) {
            if(highLow.getLow().compareTo(array[i].getLow()) > 0){
                return false;
            }
        }

        return true;

    }


    static boolean isHigh(HighLow[] array, int index, int distance){

        HighLow highLow = array[index];

        int left = index - distance;
        if(left < 0){
            left = 0;
        }
        for (int i = left; i <index ; i++) {
            if(highLow.getHigh().compareTo(array[i].getHigh()) < 0){
                return false;
            }
        }

        int right = index + distance;
        if(right > array.length){
            right = array.length;
        }

        for (int i = index +1; i <right ; i++) {
            if(highLow.getHigh().compareTo(array[i].getHigh()) < 0){
                return false;
            }
        }

        return true;

    }

    static List<IndexDecimal> getLowList(HighLow[] array, int begin, int end, int distance){
        List<IndexDecimal> lowList = new ArrayList<>();
        for (int i = begin; i <end ; i++) {
            if(isLow(array, i,distance)){
                IndexDecimal indexDecimal = new IndexDecimal(i, array[i].getLow());
                indexDecimal.setText("low");
                lowList.add(indexDecimal);
                i = i+distance-2;
            }
        }

        return lowList;
    }


    static List<IndexDecimal> getHighList(HighLow[] array, int begin, int end, int distance){
        List<IndexDecimal> list = new ArrayList<>();
        for (int i = begin; i <end ; i++) {
            if(isHigh(array, i,distance)){
                IndexDecimal indexDecimal = new IndexDecimal(i, array[i].getHigh());
                indexDecimal.setText("high");
                list.add(indexDecimal);
                i = i+distance-2;
            }
        }

        return list;
    }




}
