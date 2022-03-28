package io.runon.trading.order;

import io.runon.trading.strategy.Position;

import java.math.BigDecimal;

/**
 * 주문
 * @author macle
 */
public interface Order {
    OrderData NONE = new OrderData(Position.NONE, BigDecimal.ZERO);

    Position getPosition();
    BigDecimal getPrice();
}
