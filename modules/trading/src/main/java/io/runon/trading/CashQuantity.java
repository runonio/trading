package io.runon.trading;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class CashQuantity {
    BigDecimal cash;
    BigDecimal quantity;


    public CashQuantity(){

    }

    public CashQuantity(BigDecimal cash, BigDecimal quantity){
        this.cash = cash;
        this.quantity = quantity;
    }

}
