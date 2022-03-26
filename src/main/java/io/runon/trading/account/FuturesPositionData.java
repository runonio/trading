package io.runon.trading.account;

import io.runon.trading.strategy.Position;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 선물 포지션 구현체
 * @author macle
 */
@Data
public class FuturesPositionData implements FuturesPosition{

    protected String symbol;
    //평단가
    protected BigDecimal price = BigDecimal.ZERO;

    protected BigDecimal size = BigDecimal.ZERO;
    //레버리지
    protected BigDecimal leverage= BigDecimal.ZERO;
    protected Position position =  Position.NONE;

    @Override
    public String toString(){
        return  symbol + ": position: " +  position
                + ", leverage: " +  leverage.stripTrailingZeros().setScale(2, RoundingMode.DOWN).toPlainString()
                + ", size: " +  size.stripTrailingZeros().setScale(6, RoundingMode.DOWN).toPlainString()
                + ", price: " + price.stripTrailingZeros().setScale(2, RoundingMode.DOWN).toPlainString()
                ;
    }


}
