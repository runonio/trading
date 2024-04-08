package io.runon.trading.technical.analysis.indicators.adx;

import io.runon.trading.TimeNumber;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author macle
 */
@Data
public class AdxData implements TimeNumber {

    long time;

    //plus di
    BigDecimal pdi;
    //minus di
    BigDecimal mdi;

    BigDecimal dx;

    BigDecimal adx;

    @Override
    public BigDecimal getNumber() {
        return adx;
    }
}
