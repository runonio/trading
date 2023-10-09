package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 미체결 주문정보
 * @author macle
 */
public interface OpenOrder {

    /**
     * 종목
     */
    String getSymbol();
    /**
     * 가격
     */
    BigDecimal getPrice();
    
    /**
     * 미체결 수량
     */
    BigDecimal getOpenQuantity();

    /**
     * 주문수량
     */
    BigDecimal getOrderQuantity();


    /**
     * 거래유형
     */
    Trade.Type getTradeType();

    /**
     * 주문시간
     */
    long getTime();


}
