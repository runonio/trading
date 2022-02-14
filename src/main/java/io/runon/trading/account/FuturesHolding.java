package io.runon.trading.account;

import io.runon.trading.Position;
import lombok.Data;

import java.math.BigDecimal;

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

}
