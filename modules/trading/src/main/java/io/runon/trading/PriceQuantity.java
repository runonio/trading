package io.runon.trading;

import java.math.BigDecimal;

/**
 * 가격과 수량
 * @author macle
 */
public interface PriceQuantity {

    BigDecimal getPrice();
    BigDecimal getQuantity();

}
