package io.runon.trading.order;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.account.TradeAccount;
import io.runon.trading.exception.RequiredFieldException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

/**
 * 분할주문 공통 요소
 * @author macle
 */
public class SplitOrder {

    protected String symbol = null;

    protected TradeAccount account;
    protected long beginTime = -1L;

    //종료시간은 반드시 설정되어야함
    protected long endTime = -1L;


    protected OrderCase orderCase = OrderCase.MARKET;

    protected long delayTime = Times.MINUTE_1;

    public void setOrderCase(OrderCase orderCase) {
        this.orderCase = orderCase;
    }

    protected OrderBookGet orderBookGet = null;

    public void setOrderBookGet(OrderBookGet orderBookGet) {
        this.orderBookGet = orderBookGet;
    }


    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setAccount(TradeAccount account) {
        this.account = account;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }


    protected void valid(){
        if(symbol == null){
            throw new RequiredFieldException("symbol null");
        }

        if(endTime < System.currentTimeMillis()){
            throw new RequiredFieldException("end time set error");
        }


        if((orderCase == OrderCase.BID || orderCase == OrderCase.ASK) && orderBookGet == null) {
            throw new RequiredFieldException("order book data get interface null");
        }

        //시작 시간이 더 작으면 현제 시작시간으로 설정
        if(beginTime < System.currentTimeMillis()){
            beginTime = System.currentTimeMillis();
        }
    }


    //미체결 정보
    protected List<LimitOrderTrade> openOrderList = null;

    protected final Object openOrderLock = new Object();

    //체결수량
    protected BigDecimal totalTradingQuantity = BigDecimal.ZERO;

    //평단가
    protected BigDecimal totalTradingPrice = BigDecimal.ZERO;


    public BigDecimal getTotalTradingQuantity() {
        return totalTradingQuantity.add(sumCloseQuantity());
    }

    public BigDecimal getTotalTradingPrice() {
        return totalTradingPrice.add(sumClosePrice());
    }

    public BigDecimal getAverageTradingPrice(int scale) {
        if(totalTradingQuantity.compareTo(BigDecimal.ZERO) == 0 || totalTradingPrice.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }

        return totalTradingPrice.add(sumClosePrice()).divide(totalTradingQuantity.add(sumCloseQuantity()),scale, RoundingMode.HALF_UP);
    }

    public void addOpenOrder(LimitOrderTrade limitOrderTrade){
        synchronized (openOrderLock){
            if(openOrderList == null){
                openOrderList = new LinkedList<>();
            }
            openOrderList.add(limitOrderTrade);
        }

    }

    public void updateOpenOrder(){
        if(openOrderList == null){
            return;
        }

        synchronized (openOrderLock){

            LimitOrderTrade [] orders = openOrderList.toArray(new LimitOrderTrade[0]);
            for(LimitOrderTrade order : orders){
                if(order.getOpenQuantity().compareTo(BigDecimal.ZERO) <= 0){
                    openOrderList.remove(order);
                    totalTradingQuantity = totalTradingQuantity.add(order.getCloseQuantity());
                    totalTradingPrice = totalTradingPrice.add(order.getPrice().multiply(order.getCloseQuantity()));
                }
            }
        }
    }
    
    //미체결 전체 수량 얻기
    public BigDecimal sumOpenQuantity(){
        if(openOrderList == null){
            return BigDecimal.ZERO;
        }

        synchronized (openOrderLock){
            BigDecimal sum = BigDecimal.ZERO;

            for(LimitOrderTrade order : openOrderList){
                sum = sum.add(order.getOpenQuantity());
            }

            return sum;
        }

    }

    //미체결 전체 금액 얻기
    public BigDecimal sumOpenPrice(){
        if(openOrderList == null){
            return BigDecimal.ZERO;
        }
        synchronized (openOrderLock){
            BigDecimal sum = BigDecimal.ZERO;
            for(LimitOrderTrade order : openOrderList){
                sum = sum.add(order.getOpenQuantity().multiply(order.getPrice()));
            }

            return sum;
        }
    }


    public BigDecimal sumCloseQuantity() {
        if (openOrderList == null) {
            return BigDecimal.ZERO;
        }

        synchronized (openOrderLock) {
            BigDecimal sum = BigDecimal.ZERO;

            for (LimitOrderTrade order : openOrderList) {
                sum = sum.add(order.getCloseQuantity());
            }

            return sum;
        }
    }

    public BigDecimal sumClosePrice(){
        if (openOrderList == null) {
            return BigDecimal.ZERO;
        }
        synchronized (openOrderLock){
            BigDecimal sum = BigDecimal.ZERO;
            for(LimitOrderTrade order : openOrderList){
                sum = sum.add(order.getCloseQuantity().multiply(order.getPrice()));
            }
            return sum;
        }
    }

}
