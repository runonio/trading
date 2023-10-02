package io.runon.trading.order;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.runon.trading.strategy.Position;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class MarketOrderCashData implements MarketOrderCash {

    protected Position position = Position.NONE;
    protected BigDecimal cash = BigDecimal.ZERO;

    public MarketOrderCashData(){}
    public MarketOrderCashData(Position position, BigDecimal cash ){
        this.position = position;
        this.cash = cash;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }

}
