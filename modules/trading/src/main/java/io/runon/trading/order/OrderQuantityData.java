package io.runon.trading.order;

import io.runon.trading.Trade;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class OrderQuantityData implements OrderQuantity{
    protected Trade.Type tradeType = Trade.Type.NONE;
    protected BigDecimal quantity = BigDecimal.ZERO;
    protected String holdingId;

    public OrderQuantityData(){}
    public OrderQuantityData(String holdingId,  Trade.Type tradeType, BigDecimal quantity ){
        this.holdingId = holdingId;
        this.tradeType = tradeType;
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
