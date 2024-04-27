package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * @author macle
 */
public interface OrderQuantity {
    String getHoldingId();

    Trade.Type getTradeType();
    BigDecimal getQuantity();
}
