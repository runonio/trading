package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 시장가 주문 결과
 * @author macle
 */
public interface MarketPriceOrder {

    MarketPriceOrder EMPTY_MARKET_ORDER = new MarketPriceOrderEmpty();

    /**
     * 매매유형
     * @return 매매유형
     */
    Trade.Type getTradeType();

    /**
     * 체결가 얻기
     * @return 실제 체결가격
     */
    BigDecimal getTradePrice();

    /**
     * 체결 수량 얻기
     * @return 체결 수량
     */
    BigDecimal getQuantity();
    
}
