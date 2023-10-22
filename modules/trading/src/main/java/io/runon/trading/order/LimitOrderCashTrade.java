package io.runon.trading.order;

import java.math.BigDecimal;
/**
 * @author macle
 */
public interface LimitOrderCashTrade extends LimitOrderTrade{

    /**
     *
     * @return 주문후 남은금액
     */
    BigDecimal getRestCash();
}
