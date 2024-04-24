package io.runon.trading.account;

import java.math.BigDecimal;

/**
 * 선물계좌
 * @author macle
 */
public interface FuturesAccount extends Account{
    FuturesPosition getFuturesPosition(String symbol);
    void setLeverage(String symbol, BigDecimal leverage);
    BigDecimal getLeverage(String symbol);

    BigDecimal getAvailableBuyPrice(String symbol);
    BigDecimal getAvailableSellPrice(String symbol);
}
