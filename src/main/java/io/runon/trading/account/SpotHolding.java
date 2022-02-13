package io.runon.trading.account;

import io.runon.trading.Trade;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 현물 보유 종목
 * @author macle
 */
@Data
public class SpotHolding {

    protected String symbol;
    protected BigDecimal amount = BigDecimal.ZERO;

    protected final List<Trade> tradeList = new ArrayList<>();

    protected final Object lock = new Object();

    public void trade(Trade trade){
        synchronized (lock) {
            if (trade.getType() == Trade.Type.BUY) {
                //매수
                amount = amount.add(trade.getVolume());
            } else {
                //매도
                amount = amount.subtract(trade.getVolume());
            }
            tradeList.add(trade);
        }
    }
}
