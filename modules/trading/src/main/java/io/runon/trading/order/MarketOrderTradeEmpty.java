package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 시장가 주문결과
 * null 방지용 객체
 * @author macle
 */
public final class MarketOrderTradeEmpty implements MarketOrderTrade {
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

    @Override
    public BigDecimal getFee() {
        return  BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getLastClosePrice() {
        return  BigDecimal.ZERO;
    }

    @Override
    public long getOrderTime() {
        return System.currentTimeMillis();
    }

    @Override
    public long getCloseTime() {
        return System.currentTimeMillis();
    }
}
