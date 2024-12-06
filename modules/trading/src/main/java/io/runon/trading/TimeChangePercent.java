package io.runon.trading;

import java.math.BigDecimal;

/**
 * @author macle
 */
public interface TimeChangePercent extends Time {

    BigDecimal getChangePercent();
}
