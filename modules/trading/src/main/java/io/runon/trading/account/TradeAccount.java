package io.runon.trading.account;

import io.runon.trading.order.LimitOrder;
import io.runon.trading.order.MarketOrder;
import io.runon.trading.order.OpenOrder;

/**
 * 게좌
 * 주문기능 포함
 * @author macle
 */
public interface TradeAccount extends Account, MarketOrder, LimitOrder {

    /**
     * 미체결된 주문(종목)
     */
    OpenOrder getOpenOrder(String symbol);

    /**
     * 미체결된 전체정보
     */
    OpenOrder [] getOpenOrders();

}
