package io.runon.trading;

import java.math.BigDecimal;

/**
 * @author macle
 */
public interface Volume {
    BigDecimal getVolume();
    BigDecimal getAmount();
    BigDecimal getVolumePower();
}
