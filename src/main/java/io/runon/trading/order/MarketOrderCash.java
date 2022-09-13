package io.runon.trading.order;

import io.runon.trading.strategy.Position;

import java.math.BigDecimal;

/**
 * 현금을 활용한 시장가 주문
 * @author macle
 */
public interface MarketOrderCash {
    MarketOrderCashData NONE = new MarketOrderCashData(Position.NONE, BigDecimal.ZERO);

    Position getPosition();
    BigDecimal getCash();
}
