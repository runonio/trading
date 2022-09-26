package io.runon.trading.technical.analysis.indicators.sar;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author macle
 */
@Data
public class SarData {

    long time;

    BigDecimal advancingAf;

    BigDecimal declineAf;

    //상승 할때의 sar
    BigDecimal advancing;
    //하락 할때의 sar
    BigDecimal decline;
}
