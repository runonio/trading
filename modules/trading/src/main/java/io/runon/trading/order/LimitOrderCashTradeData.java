package io.runon.trading.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * @author macle
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LimitOrderCashTradeData extends LimitOrderTradeData implements LimitOrderCashTrade{
    protected BigDecimal restCash;
}
