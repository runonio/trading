package io.runon.trading.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 시장가 구현체
 * @author macle
 */
@Data
public class MarketPriceOrderData implements MarketPriceOrder{

    protected BigDecimal tradePrice;

    @Override
    public BigDecimal getTradePrice() {
        return tradePrice;
    }
}
