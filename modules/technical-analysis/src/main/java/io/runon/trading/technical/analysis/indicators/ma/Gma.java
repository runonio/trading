package io.runon.trading.technical.analysis.indicators.ma;

import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.hl.HighLowLineLeftSearch;
import io.runon.trading.technical.analysis.indicators.NIndicators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 기울기 이동평균
 * Gradient Moving Average
 * 다이버전스를 어떻게 구하는게 좋을지 연구.
 *
 * @author macle
 */
public class Gma extends NIndicators<TimeNumber> {

    int initN  = 200 ;
    int continueN = 10;

    public Gma(){
        scale =4;
    }

    public void setInitN(int initN) {
        this.initN = initN;
    }

    public void setContinueN(int continueN) {
        this.continueN = continueN;
    }

    @Override
    public BigDecimal get(TimeNumber[] array, int n, int index) {

        int highIndex = HighLowLineLeftSearch.searchHigh(array, initN, continueN, index);
        int lowIndex = HighLowLineLeftSearch.searchLow(array, initN, continueN, index);

        BigDecimal height = array[highIndex].getNumber().subtract(array[lowIndex].getNumber());

        if(height.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }

        int compareIndex = index - n;
        if(compareIndex < 0){
            compareIndex = 0;
        }

        BigDecimal move =  array[index].getNumber().subtract(array[compareIndex].getNumber());

        if(move.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }

        int length = index - compareIndex+1;

        return height.divide(move.multiply(new BigDecimal(length)), MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
    }

}
