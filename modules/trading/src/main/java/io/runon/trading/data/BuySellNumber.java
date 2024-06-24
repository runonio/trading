package io.runon.trading.data;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class BuySellNumber {

    BigDecimal buy;

    BigDecimal sell;

    public BuySellNumber(){

    }
    public BuySellNumber(BigDecimal buy, BigDecimal sell){
        this.buy = buy;
        this.sell = sell;
    }

    public BigDecimal getGap() {
        return buy.subtract(sell);
    }
}
