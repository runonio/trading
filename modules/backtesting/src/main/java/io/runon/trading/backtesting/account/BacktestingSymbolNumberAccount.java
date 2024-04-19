package io.runon.trading.backtesting.account;

import io.runon.trading.backtesting.price.symbol.SymbolPrice;
import io.runon.trading.symbol.SymbolNumber;

import java.math.BigDecimal;

/**
 * 선물백테스팅 거래 계좌
 * 레버리지 1배고정
 * @author macle
 */
public class BacktestingSymbolNumberAccount extends BacktestingSymbolAccount<SymbolNumber> {
    public BacktestingSymbolNumberAccount(SymbolPrice symbolPrice) {
        super(symbolPrice);
    }

    @Override
    public SymbolNumber newSymbolNumber(String symbol, BigDecimal number) {
        return new SymbolNumber(symbol, number);
    }
}
