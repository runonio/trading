package io.runon.trading.order;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.time.Times;
import io.runon.trading.PriceQuantity;
import io.runon.trading.Trade;
import io.runon.trading.account.Account;
import io.runon.trading.account.TradeAccount;
import io.runon.trading.exception.RequiredFieldException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 분할매수
 * 시장가 주문이 아닌 경우
 * 원하는 수량이 채워지지 않을 수 있다.
 * 시간을 동일하게 나누어서 사는것 부터 구현한다.
 * 추상체로 여러 옵션을 받아서 구현할 계획이지만 단순 시간을 나눠서 사용하는 기본구현체 부터 제공한다.
 * 관련 클래스는 클래스명 메소드명이 버전이 바뀌면 변경될 수 있다.
 * @author macle
 */
@Slf4j
public class SplitBuy {

    //종목코드
    protected String symbol = null;

    protected TradeAccount account;
    protected long beginTime = -1L;

    //종료시간은 반드시 설정되어야함
    protected long endTime = -1L;

    protected BigDecimal buyTotalCash = null;

    protected OrderCase orderCase = OrderCase.MARKET;

    protected long delayTime = Times.MINUTE_1;

    public void setOrderCase(OrderCase orderCase) {
        this.orderCase = orderCase;
    }

    protected OrderBookGet orderBookGet = null;

    public void setOrderBookGet(OrderBookGet orderBookGet) {
        this.orderBookGet = orderBookGet;
    }

    public SplitBuy(){

    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setBuyTotalCash(BigDecimal buyTotalCash) {
        this.buyTotalCash = buyTotalCash;
    }

    public void setAccount(TradeAccount account) {
        this.account = account;
    }

    protected int orderScale = 5;

    /**
     * 주문금액 소수점
     */
    public void setOrderScale(int orderScale) {
        this.orderScale = orderScale;
    }

    //체결수량
    protected BigDecimal tradingQuantity;

    //평단가
    protected BigDecimal averageTradingPrice;


    public void buy(){
        if(symbol == null){
            throw new RequiredFieldException("symbol null");
        }

        if(endTime < System.currentTimeMillis()){
            throw new RequiredFieldException("end time set error");
        }

        if(buyTotalCash == null || buyTotalCash.compareTo(BigDecimal.ZERO) <= 0){
            throw new RequiredFieldException("buy cash set error");
        }

        if(orderCase == OrderCase.BID && orderBookGet == null) {
            throw new RequiredFieldException("order book data get interface null");
        }

        //시작 시간이 더 작으면 현제 시작시간으로 설정
        if(beginTime < System.currentTimeMillis()){
            beginTime = System.currentTimeMillis();
        }

        BigDecimal buyCash = buyTotalCash;

        //주문횟수
        long orderCount = (endTime - beginTime)/delayTime;

        BigDecimal orderBuyCash = buyCash.divide(new BigDecimal(orderCount), orderScale, RoundingMode.DOWN);

        for(;;){
            if(endTime <= System.currentTimeMillis()){
                break;
            }
            try {
                trade(orderBuyCash);
                Thread.sleep(delayTime);
            }catch (Exception e){
                log.error(ExceptionUtil.getStackTrace(e));
                break;
            }
        }

    }

    public void trade(BigDecimal orderCash){
        if(orderCase == OrderCase.MARKET){
            account.marketOrderCash(symbol, Trade.Type.BUY, orderCash);
        }else if(orderCase == OrderCase.BID){
            //호가를 얻어와서
            OrderBook orderBook = orderBookGet.getOrderBook();
            PriceQuantity[] bids = orderBook.getBids();

            if(bids == null || bids.length == 0){
                //호가창을 가져오지 못한경우 현제 거래된 가격으로 매수주문
                account.limitOrderCash(symbol, Trade.Type.BUY, orderCash, account.getPrice(symbol));
            }else{

                account.limitOrderCash(symbol, Trade.Type.BUY, orderCash, bids[0].getPrice());
            }
        }
    }
}
