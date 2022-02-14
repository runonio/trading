package io.runon.trading.account;

import io.runon.trading.Position;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 선물거래 종목
 * @author macle
 */
@Data
public class FuturesHolding {


    protected String symbol;
    protected BigDecimal price;
    protected BigDecimal amount;
    //레버리지
    protected BigDecimal leverage;
    protected Position position;

    @Override
    public String toString(){
        return "price: " + price.stripTrailingZeros().setScale(2, RoundingMode.DOWN).toPlainString()
                +", amount: " +  amount.stripTrailingZeros().setScale(6, RoundingMode.DOWN).toPlainString()
                +", leverage: " +  leverage.stripTrailingZeros().setScale(2, RoundingMode.DOWN).toPlainString()
                +", position: " +  position;
    }

}
