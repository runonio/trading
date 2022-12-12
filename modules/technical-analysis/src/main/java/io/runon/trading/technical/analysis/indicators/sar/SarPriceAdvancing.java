package io.runon.trading.technical.analysis.indicators.sar;

import com.seomse.commons.config.Config;

import java.math.BigDecimal;

/**
 * 가격정보를 활용한 상승추세일때의
 * 시간 가격정보 활용(종가정보)
 * SAR
 * @author macle
 */
public class SarPriceAdvancing {

    private BigDecimal af = new BigDecimal(Config.getConfig("sar.af", "0.02"));
    private BigDecimal maxAf = new BigDecimal(Config.getConfig("sar.af.max", "0.2"));

    private BigDecimal ep;

    public void setAf(BigDecimal af) {
        this.af = af;
    }

    public void setMaxAf(BigDecimal maxAf) {
        this.maxAf = maxAf;
    }

    public void setEp(BigDecimal ep) {
        this.ep = ep;
    }

    public void setEp(){

    }


}
