package io.runon.trading.technical.analysis.hl;

import java.math.BigDecimal;
/**
 * @author macle
 */
public class HlcData extends HighLow{


    BigDecimal change;
    //높이 대비 변화율
    BigDecimal rate;

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
}
