package io.runon.trading.account;

import io.runon.trading.order.LimitOrder;
import io.runon.trading.order.LimitOrderTrade;
import io.runon.trading.order.MarketOrder;
import io.runon.trading.order.OpenOrder;

import java.math.BigDecimal;

/**
 * 게좌
 * 주문기능 포함
 * @author macle
 */
public interface TradeAccount extends Account, MarketOrder, LimitOrder {

    /**
     * 미체결된 주문(종목)
     * 미체결 주문은 지정가로 하기때문에 지정가 주문 내역이 된다.
     */
    LimitOrderTrade[] getOpenOrders(String symbol);

    /**
     * 미체결된 전체정보
     */
    OpenOrder [] getOpenOrders();

    /**
     * 현제가격 얻기
     */
    BigDecimal getPrice(String symbol);

}
