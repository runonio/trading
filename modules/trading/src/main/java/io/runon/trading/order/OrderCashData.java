package io.runon.trading.order;

import io.runon.trading.strategy.Position;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class OrderCashData implements OrderCash{
    BigDecimal cash;
    Position position;

    public OrderCashData(Position position, BigDecimal cash){
        this.cash = cash;
        this.position = position;
    }
}
