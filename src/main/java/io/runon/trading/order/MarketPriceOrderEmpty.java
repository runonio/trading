package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 시장가 주문결과
 * null 방지용 객체
 * @author macle
 */
public final class MarketPriceOrderEmpty implements MarketPriceOrder{
    @Override
    public Trade.Type getTradeType() {
        return Trade.Type.NONE;
    }

    @Override
    public BigDecimal getTradePrice() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getQuantity() {
        return BigDecimal.ZERO;
    }
}
