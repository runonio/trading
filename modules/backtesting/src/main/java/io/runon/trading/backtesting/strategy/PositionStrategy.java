package io.runon.trading.backtesting.strategy;

import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.Strategy;

/**
 * @author macle
 */
public class PositionStrategy<E> implements TradingStrategy<E>{

    private final Strategy<E> strategy;

    public PositionStrategy (Strategy<E> strategy){
        this.strategy = strategy;
    }

    Position last = Position.NONE;

    @Override
    public Position getPosition(E data) {
        last = strategy.getPosition(data);
        return last;
    }

    @Override
    public void trade(FuturesBacktestingAccount account, String symbol) {
        if(last == Position.LONG){
            //숏매도 롱매수
            account.buyAll(symbol);
        }else if(last == Position.SHORT){
            //롱매도 숏매수
            account.sellAll(symbol);
        }else if(last == Position.LONG_CLOSE){
            //롱매도
            account.longClose(symbol);
        }else if(last == Position.SHORT_CLOSE){
            //숏매도
            account.shortClose(symbol);
        }else if(last == Position.CLOSE){
            //롱 숏 둘다매도
            account.close(symbol);
        }
    }
}
