package io.runon.trading.order;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.runon.trading.Trade;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 시장가 구현체
 * @author macle
 */
@Data
public class MarketOrderTradeData implements MarketOrderTrade {
    protected BigDecimal tradePrice;
    protected BigDecimal quantity;
    protected Trade.Type tradeType;
    protected BigDecimal fee;
    protected BigDecimal lastClosePrice;
    protected long orderTime =-1;
    protected long closeTime =-1;
    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}
