package io.runon.trading.backtesting.strategy;

import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.strategy.Position;

/**
 * 매매전략
 * @author macle
 */
public interface TradingStrategy<E> {
    Position getPosition(E data);
    void trade(FuturesBacktestingAccount account,  String symbol);
}
