package io.runon.trading.backtesting.strategy;

import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.order.OrderCash;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.StrategyOrderCash;

/**
 * @author macle
 */
public class OrderStrategy<E> implements TradingStrategy<E>{
    private final StrategyOrderCash<E> strategy;

    public OrderStrategy(StrategyOrderCash<E> strategy){
        this.strategy = strategy;
    }

    private OrderCash last = OrderCash.NONE;

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
