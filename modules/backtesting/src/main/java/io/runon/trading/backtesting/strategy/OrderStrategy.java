package io.runon.trading.backtesting.strategy;

import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.order.MarketOrderCash;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.StrategyOrder;

/**
 * @author macle
 */
public class OrderStrategy<E> implements TradingStrategy<E>{
    private final StrategyOrder<E> strategy;

    public OrderStrategy(StrategyOrder<E> strategy){
        this.strategy = strategy;
    }

    private MarketOrderCash last = MarketOrderCash.NONE;

    @Override
    public Position getPosition(E data) {
        last = strategy.getPosition(data);
        return last.getPosition();
    }

    @Override
    public void trade(FuturesBacktestingAccount account, String symbol) {
        account.order(symbol, last);
    }
}
