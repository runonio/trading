package io.runon.trading.technical.analysis.hl.hlnc;

import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;
import io.runon.trading.technical.analysis.indicators.NTimeIndicators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * High Low Change Middle
 * 고가 저가 대비 중간가격의 변화량
 * @author macle
 */
public class Hlncm extends NTimeIndicators<HlncData, CandleStick> {

    int initN  = 200 ;
    int continueN = 10;

    public Hlncm(){
        scale =4;
    }

    public void setInitN(int initN) {
        this.initN = initN;
    }

    public void setContinueN(int continueN) {
        this.continueN = continueN;
    }
    @Override
    public HlncData get(CandleStick[] array, int n, int index) {
        HlncData hlcData = new HlncData();

        int highIndex = HighLowCandleLeftSearch.searchHigh(array, initN, continueN, index);
        int lowIndex = HighLowCandleLeftSearch.searchLow(array, initN, continueN, index);

        CandleStick highCandle = array[highIndex];
        CandleStick lowCandle = array[lowIndex];

        hlcData.setTime(array[index].getTime());

        hlcData.setHigh( highCandle.getHigh());
        hlcData.setHighTime(highCandle.getTime());
        hlcData.setHighIndex(highIndex);

        hlcData.setLow(lowCandle.getLow());
        hlcData.setLowTime(lowCandle.getTime());
        hlcData.setLowIndex(lowIndex);

        BigDecimal height = highCandle.getHigh().subtract(lowCandle.getLow());
        hlcData.setHeight(height);
        if(height.compareTo(BigDecimal.ZERO) == 0){
            return hlcData;
        }

        int compareIndex = index - n;
        if(compareIndex < 0){
            compareIndex = 0;
        }

        BigDecimal change =  array[index].getMiddle().subtract(array[compareIndex].getMiddle());
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