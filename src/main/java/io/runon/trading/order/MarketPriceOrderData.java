package io.runon.trading.order;

import io.runon.trading.Trade;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 시장가 구현체
 * @author macle
 */
@Data
public class MarketPriceOrderData implements MarketPriceOrder{
    protected BigDecimal tradePrice;
    protected BigDecimal quantity;
    protected Trade.Type tradeType;

}
