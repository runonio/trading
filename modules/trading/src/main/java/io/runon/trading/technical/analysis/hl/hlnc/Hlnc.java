package io.runon.trading.technical.analysis.hl.hlnc;

import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.hl.HighLowLineLeftSearch;
import io.runon.trading.technical.analysis.indicators.NTimeIndicators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * High Low Change 
 * 고가 저가 대비 변화량
 * @author macle
 */
public class Hlnc extends NTimeIndicators<HlncData, TimeNumber> {

    int initN  = 200 ;
    int continueN = 10;

    public Hlnc(){
        scale =4;
    }

    public void setInitN(int initN) {
        this.initN = initN;
    }

    public void setContinueN(int continueN) {
        this.continueN = continueN;
    }
    @Override
    public HlncData get(TimeNumber[] array, int n, int index) {
        HlncData hlcData = new HlncData();

        int highIndex = HighLowLineLeftSearch.searchHigh(array, initN, continueN, index);
        int lowIndex = HighLowLineLeftSearch.searchLow(array, initN, continueN, index);

        TimeNumber highTimeNumber = array[highIndex];
        TimeNumber lowTimeNumber = array[lowIndex];

        hlcData.setTime(array[index].getTime());

        hlcData.setHigh( highTimeNumber.getNumber());
        hlcData.setHighTime(highTimeNumber.getTime());
        hlcData.setHighIndex(highIndex);

        hlcData.setLow( lowTimeNumber.getNumber());
        hlcData.setLowTime(lowTimeNumber.getTime());
        hlcData.setLowIndex(lowIndex);

        BigDecimal height = highTimeNumber.getNumber().subtract(lowTimeNumber.getNumber());
        hlcData.setHeight(height);
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
        hlcData.rate = change.divide(height, MathContext.DECIMAL128).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
        return hlcData;
    }

    @Override
    public HlncData[] newArray(int length) {
        if(length < 1){
            return HlncData.EMPTY_ARRAY;
        }

        return new HlncData[length];
    }



}