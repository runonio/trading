package io.runon.trading.technical.analysis.hl;

import io.runon.trading.TimeNumber;

import java.math.BigDecimal;
/**
 * @author macle
 */
public class HlcData extends HighLow implements TimeNumber {

    public static final HlcData [] EMPTY_ARRAY = new HlcData[0];

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
}
