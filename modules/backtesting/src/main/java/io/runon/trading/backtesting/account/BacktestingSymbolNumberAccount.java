package io.runon.trading.backtesting.account;

import io.runon.trading.symbol.SymbolNumber;

import java.math.BigDecimal;

/**
 * 선물백테스팅 거래 계좌
 * 레버리지 1배고정
 * @author macle
 */
public class BacktestingSymbolNumberAccount extends BacktestingHoldingAccount<SymbolNumber> {
    public BacktestingSymbolNumberAccount() {
        super();
    }

    @Override
    public BigDecimal getPrice(String symbol) {
        return idPrice.getPrice(symbol);
    }

    @Override
    public SymbolNumber newHoldingQuantity(String id, BigDecimal quantity) {
        return new SymbolNumber(id, quantity);
    }

}
