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
    //포지션
    protected Position position =  Position.NONE;
    //매매대금 (실제 달러)
    protected BigDecimal tradingPrice = BigDecimal.ZERO;
    //평단가
    protected BigDecimal price = BigDecimal.ZERO;

    //수량
    protected BigDecimal quantity = BigDecimal.ZERO;
    
    //레버리지
    protected BigDecimal leverage= BigDecimal.ZERO;



    @Override
    public String toString(){
        return  symbol + ": position: " +  position
                + ", tradingPrice: " +  tradingPrice.setScale(2, RoundingMode.DOWN).stripTrailingZeros().toPlainString()
                + ", price: " + price.setScale(2, RoundingMode.DOWN).stripTrailingZeros().toPlainString()
                + ", quantity: " +  quantity.setScale(6, RoundingMode.DOWN).stripTrailingZeros().toPlainString()
                + ", leverage: " +  leverage.setScale(2, RoundingMode.DOWN).stripTrailingZeros().toPlainString()
                ;
    }
    
}
