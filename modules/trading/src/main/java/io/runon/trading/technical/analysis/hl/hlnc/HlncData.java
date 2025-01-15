package io.runon.trading.technical.analysis.hl.hlnc;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.hl.HighLowTime;

import java.math.BigDecimal;
/**
 * @author macle
 */
public class HlncData extends HighLowTime implements TimeNumber {

    public static final HlncData[] EMPTY_ARRAY = new HlncData[0];

    BigDecimal change = BigDecimal.ZERO;
    //높이 대비 변화율
    BigDecimal rate = BigDecimal.ZERO;

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal getNumber() {
        return rate;
    }

    public static TimeNumber[] getHeights(HlncData[] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            HlncData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.height);
        }

        return timeNumbers;
    }
}
