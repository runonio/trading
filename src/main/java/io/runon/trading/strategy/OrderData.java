package io.runon.trading.strategy;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class OrderData implements Order{

    protected Position position = Position.NONE;
    protected BigDecimal price = BigDecimal.ZERO;

    public OrderData(){}
    public OrderData(Position position, BigDecimal price ){
        this.position = position;
        this.price = price;
    }

}
