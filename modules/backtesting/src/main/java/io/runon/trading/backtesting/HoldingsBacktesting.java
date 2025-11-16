package io.runon.trading.backtesting;

import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtils;
import io.runon.trading.*;
import io.runon.trading.backtesting.account.BacktestingHoldingAccount;
import io.runon.trading.closed.days.ClosedDays;
import io.runon.trading.order.OrderQuantity;
import io.runon.trading.strategy.StrategyOrders;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 보유종목이 많을 떄의 벡테스팅
 * @author macle
 */
@Slf4j
public class HoldingsBacktesting<E extends BacktestingData, T extends HoldingQuantity> implements Runnable{

    protected StrategyOrders<E> strategy;

    protected E data;

    protected int cashScale = 0;

    protected BacktestingHoldingAccount<T> account;

    protected long beginTime = -1;
    protected long endTime = -1;

    protected long moveTime = -1;

    protected ZoneId zoneId = TradingTimes.KOR_ZONE_ID;

    protected boolean isLog = true;

    private ClosedDays closedDays = null;

    public void setClosedDays(ClosedDays closedDays) {
        this.closedDays = closedDays;
    }


    protected TimeNumber [] assetTimes ;

    public HoldingsBacktesting(){
    }


    public void setStrategy(StrategyOrders<E> strategy) {
        this.strategy = strategy;
    }

    public void setLog(boolean log) {
        isLog = log;
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

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
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

        init();

        long nextTime= beginTime;
        List<TimeNumber> assetTimeList = new ArrayList<>();
        for(;;){

            if(closedDays.isClosedDay(YmdUtils.getYmd(nextTime, zoneId))){
                nextTime += moveTime;
                if(nextTime > endTime){
                    break;
                }
                continue;
            }

            data.setBaseTime(nextTime);
            strategy();

            if(isLog) {
                //초기에는 콘솔에 뿌리기, 나중에 리포트 형슥의 결과를 제공 하는 경우를 고민한다.
                log.debug(Times.ymdhm(nextTime, zoneId));
                log.debug(account.getAssets().stripTrailingZeros().toPlainString());
            }
            assetTimeList.add(new TimeNumberData(nextTime, account.getAssets().stripTrailingZeros()));

            nextTime += moveTime;
            if(nextTime > endTime){
                break;
            }
        }

        assetTimes = assetTimeList.toArray(new TimeNumber[assetTimeList.size()]);
        assetTimeList.clear();
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

    protected void init(){

    }

    public E getData() {
        return data;
    }

    public StrategyOrders<E> getStrategy() {
        return strategy;
    }

    public TimeNumber[] getAssetTimes() {
        return assetTimes;
    }
}
