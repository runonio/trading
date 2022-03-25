package io.runon.trading.account;

import io.runon.trading.strategy.Position;

import java.math.BigDecimal;

/**
 * 선물 포지션
 * @author macle
 */
public interface FuturesPosition {
    String getSymbol();
    BigDecimal getPrice();
    BigDecimal getSize();
    BigDecimal getLeverage();
    Position getPosition();
}
