package io.runon.trading.technical.analysis.hl;

import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.indicators.NTimeIndicators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * High Low Change 
 * 고가 저가 대비 변화량
 * @author macle
 */
public class Hlc extends NTimeIndicators<HlcData, TimeNumber> {

    int initN  = 200 ;
    int continueN = 10;

    public Hlc(){
        scale =4;
    }

    public void setInitN(int initN) {
        this.initN = initN;
    }

    public void setContinueN(int continueN) {
        this.continueN = continueN;
    }
    @Override
    public HlcData get(TimeNumber[] array, int n, int index) {
        HlcData hlcData = new HlcData();

        int highIndex = HighLowLineLeftSearch.searchHigh(array, initN, continueN, index);
        int lowIndex = HighLowLineLeftSearch.searchLow(array, initN, continueN, index);

        TimeNumber highTimeNumber = array[highIndex];
        TimeNumber lowTimeNumber = array[lowIndex];

        hlcData.time = array[index].getTime();

        hlcData.high = highTimeNumber.getNumber();
        hlcData.highTime = highTimeNumber.getTime();
        hlcData.highIndex = highIndex;

        hlcData.low = lowTimeNumber.getNumber();
        hlcData.lowTime = lowTimeNumber.getTime();
        hlcData.lowIndex = lowIndex;

        BigDecimal height = highTimeNumber.getNumber().subtract(lowTimeNumber.getNumber());

        if(height.compareTo(BigDecimal.ZERO) == 0){
            return hlcData;
        }

        int compareIndex = index - n;
        if(compareIndex < 0){
            compareIndex = 0;
        }

        BigDecimal change =  array[index].getNumber().subtract(array[compareIndex].getNumber());
        hlcData.change = change;
        if(change.compareTo(BigDecimal.ZERO) == 0){
            return hlcData;
        }

        int length = index - compareIndex+1;
        hlcData.rate = height.divide(change, MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
        return hlcData;
    }

    @Override
    public HlcData[] newArray(int length) {
        if(length < 1){
            return HlcData.EMPTY_ARRAY;
        }

        return new HlcData[length];
    }
}