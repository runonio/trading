package io.runon.trading;

import java.math.BigDecimal;

/**
 * @author macle
 */
public interface PriceGet {
    BigDecimal getPrice(String id);
}
