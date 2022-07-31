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
public class OrderData implements Order {

    protected Position position = Position.NONE;
    protected BigDecimal price = BigDecimal.ZERO;

    public OrderData(){}
    public OrderData(Position position, BigDecimal price ){
        this.position = position;
        this.price = price;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }

}
