package io.runon.trading.technical.analysis.indicators.elder;

import io.runon.trading.TimeNumber;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class ForceIndexData implements TimeNumber {

    long time;

    BigDecimal forceIndex;
    BigDecimal shortIndex;
    BigDecimal longIndex;


    @Override
    public BigDecimal getNumber() {
        return forceIndex;
    }
}
