package io.runon.trading.strategy;

import io.runon.trading.order.OrderQuantity;

/**
 * 전략
 * @author macle
 */
public interface StrategyOrders<E> {
    OrderQuantity[] getOrders(E data);
}
