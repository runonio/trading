package io.runon.trading.backtesting;

import io.runon.trading.CashQuantity;
import io.runon.trading.HoldingQuantity;
import io.runon.trading.Trade;
import io.runon.trading.backtesting.account.BacktestingHoldingAccount;
import io.runon.trading.order.OrderQuantity;
import io.runon.trading.strategy.StrategyOrders;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 켄들을 활용해 벡테스팅한다..
 * @author macle
 */
@Slf4j
public class CandleBacktesting<E extends BacktestingData, T extends HoldingQuantity> implements Runnable{

    protected StrategyOrders<E> strategy;

    protected E data;

    protected int cashScale = 0;

    protected BacktestingHoldingAccount<T> account;

    protected long beginTime = -1;
    protected long endTime = -1;

    protected long moveTime = -1;

    public CandleBacktesting(){
    }


    public void setStrategy(StrategyOrders<E> strategy) {
        this.strategy = strategy;
    }

    public void setCashScale(int cashScale) {
        this.cashScale = cashScale;
    }

    public void setAccount(BacktestingHoldingAccount<T> account) {
        this.account = account;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setMoveTime(long moveTime) {
        this.moveTime = moveTime;
    }

    public void setData(E data) {
        this.data = data;
    }

    @Override
    public void run(){

        if(beginTime < 0){
            throw new IllegalArgumentException("begin time >= 0 : " + beginTime);
        }

        if(endTime < beginTime){
            throw new IllegalArgumentException("end time > begin time ");
        }

        if(moveTime < 1){
            throw new IllegalArgumentException("move time > 0 : " + moveTime);
        }

        if(account == null){
            throw new IllegalArgumentException("account null");
        }

        if(strategy == null){
            throw new IllegalArgumentException("strategy null");
        }

        if(data == null){
            throw new IllegalArgumentException("strategy null");
        }

        long nextTime= beginTime;

        //noinspection ConditionalBreakInInfiniteLoop
        for(;;){
            data.setStandardTime(nextTime);
            strategy();

            //초기에는 콘솔에 뿌리기, 나중에 리포트 형슥의 결과를 제공 하는 경우를 고민한다.

            nextTime += moveTime;
            if(moveTime > endTime){
                break;
            }

        }
    }

    private void strategy(){
        OrderQuantity[] orders = strategy.getOrders(data);

        if(orders == null || orders.length == 0){
            return;
        }


        //마지막 현금 저장
        //매매도중에 증가한 현금을 활용하면 벡테스팅 오류.
        BigDecimal cash = account.getCash();

        for(OrderQuantity order : orders){
            Trade.Type tradeType = order.getTradeType();
            if(tradeType == Trade.Type.NONE){
                continue;
            }

            if(tradeType == Trade.Type.BUY) {

                CashQuantity cashQuantity = account.getCashQuantity(order.getHoldingId(), order.getQuantity(), cash);

                account.buy(order.getHoldingId(), cashQuantity.getQuantity());

                cash = cash.subtract(cashQuantity.getCash());

            }else if(tradeType == Trade.Type.SELL){
                account.sell(order.getHoldingId(), order.getQuantity());
            }

        }


    }



}
