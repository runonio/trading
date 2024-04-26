package io.runon.trading.order;

import io.runon.trading.strategy.Position;

import java.math.BigDecimal;

/**
 * @author macle
 */
public interface OrderCash {
    OrderCash NONE = new OrderCashData(Position.NONE, BigDecimal.ZERO );
    BigDecimal getCash();
    Position getPosition();
}
