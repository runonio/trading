package io.runon.trading.technical.analysis.hl;

import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.indicators.NTimeIndicators;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * High Low Change Middle
 * 고가 저가 대비 중간가격의 변화량
 * @author macle
 */
public class Hlcm extends NTimeIndicators<HlcData, CandleStick> {

    int initN  = 200 ;
    int continueN = 10;

    public Hlcm(){
        scale =4;
    }

    public void setInitN(int initN) {
        this.initN = initN;
    }

    public void setContinueN(int continueN) {
        this.continueN = continueN;
    }
    @Override
    public HlcData get(CandleStick[] array, int n, int index) {
        HlcData hlcData = new HlcData();

        int highIndex = HighLowCandleLeftSearch.searchHigh(array, initN, continueN, index);
        int lowIndex = HighLowCandleLeftSearch.searchLow(array, initN, continueN, index);

        CandleStick highCandle = array[highIndex];
        CandleStick lowCandle = array[lowIndex];

        hlcData.time = array[index].getTime();

        hlcData.high = highCandle.getHigh();
        hlcData.highTime = highCandle.getTime();
        hlcData.highIndex = highIndex;

        hlcData.low = lowCandle.getLow();
        hlcData.lowTime = lowCandle.getTime();
        hlcData.lowIndex = lowIndex;

        BigDecimal height = highCandle.getHeight().subtract(lowCandle.getLow());

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