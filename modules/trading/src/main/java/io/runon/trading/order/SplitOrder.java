package io.runon.trading.order;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.PriceQuantity;
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
public abstract class SplitOrder {

    protected String symbol = null;

    protected TradeAccount account;
    protected long beginTime = -1L;

    //종료시간은 반드시 설정되어야함
    protected long endTime = -1L;

    protected int orderScale = 5;

    //재주문 시간
    protected long reorderTime = -1;

    public void setReorderTime(long reorderTime) {
        this.reorderTime = reorderTime;
    }

    protected BigDecimal minQuantity = null;


    public BigDecimal getMinQuantity() {
        if(minQuantity == null){
            if(orderScale == 0){
                minQuantity = new BigDecimal(1);
            }else{
                StringBuilder sb = new StringBuilder();
                sb.append("0.");
                //noinspection StringRepeatCanBeUsed
                for (int i = 1; i <orderScale ; i++) {
                    sb.append("0");
                }
                sb.append("1");
                minQuantity = new BigDecimal(sb.toString());

            }
        }

        return minQuantity;
    }

    /**
     * 주문 소스점 설정
     */
    public void setOrderScale(int orderScale) {
        this.orderScale = orderScale;
    }

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

        if(orderScale < 0){
            throw new RequiredFieldException("order scale less than 0");
        }

        //시작 시간이 더 작으면 현제 시작시간으로 설정
        if(beginTime < System.currentTimeMillis()){
            beginTime = System.currentTimeMillis();
        }

        if(reorderTime == -1){
            reorderTime = delayTime*5;
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
                }else{
                    if(reorderTime > 0 && System.currentTimeMillis() - order.getOrderTime() > reorderTime){
                        //재주문 시간 초과인경우
                        //금액 정산후 주문 취소
                        openOrderList.remove(order);
                        totalTradingQuantity = totalTradingQuantity.add(order.getCloseQuantity());
                        totalTradingPrice = totalTradingPrice.add(order.getPrice().multiply(order.getCloseQuantity()));
                        openOrderCancel(order);
                    }
                }


            }
        }
    }


    /**
     * 미체결 주문 취소 처리
     * @param limitOrderTrade 미체결 주문 취소 처리
     */
    public abstract void openOrderCancel(LimitOrderTrade limitOrderTrade );

    
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

    /**
     * 
     * @return 주문당시가격 (추정치)
     */
    public BigDecimal getOrderPrice(){
        if(orderCase == OrderCase.MARKET){
            return account.getPrice(symbol);
        }else if(orderCase == OrderCase.BID){
            OrderBook orderBook = orderBookGet.getOrderBook();
            PriceQuantity[] bids = orderBook.getBids();

            if(bids == null || bids.length == 0){
                return account.getPrice(symbol);
            }else{
                return  bids[0].getPrice();
            }
        }else if(orderCase == OrderCase.ASK){
            OrderBook orderBook = orderBookGet.getOrderBook();
            PriceQuantity [] asks = orderBook.getAsks();

            if(asks == null || asks.length == 0){
                return account.getPrice(symbol);
            }else{
                return asks[0].getPrice();
            }
        }
        return null;
    }

}
