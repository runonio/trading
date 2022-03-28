package io.runon.trading.order;

import java.math.BigDecimal;
/**
 * @author macle
 */
public interface OrderPrice<E> {
    BigDecimal getOrderPrice(E data);
}
