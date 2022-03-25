package io.runon.trading.order;

import java.math.BigDecimal;

/**
 * 시장가 주문
 * @author macle
 */
public interface MarketPriceOrder {
    /**
     * 체결가 얻기
     * @return 실제 체결가격
     */
    BigDecimal getTradePrice();
}
