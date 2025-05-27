package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 지정가 주문
 * @author macle
 */
public interface LimitOrder {

    LimitOrderTrade limitOrderQuantity(String id,  String exchange, Trade.Type type, BigDecimal quantity, BigDecimal limitPrice);


    LimitOrderTrade limitOrderCash(String id,  String exchange, Trade.Type type, BigDecimal cash, BigDecimal limitPrice);


    LimitOrderTrade limitOrderCash(String id, String exchange, Trade.Type type, BigDecimal cash, BigDecimal beginPrice, BigDecimal endPrice, BigDecimal priceGap );



}
