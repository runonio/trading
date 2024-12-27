package backup;

import io.runon.trading.Trade;
import io.runon.trading.exception.RequiredFieldException;
import io.runon.trading.order.LimitOrderCashTrade;
import io.runon.trading.order.LimitOrderTrade;
import io.runon.trading.order.MarketOrderTrade;
import io.runon.trading.order.OrderCase;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 분할매수
 * 매수할때는 현금을 활용 (수량 버전은 필요가 있을때 이후 개발)
 * 시장가 주문이 아닌 경우
 * 원하는 수량이 채워지지 않을 수 있다.
 * 시간을 동일하게 나누어서 사는것 부터 구현한다.
 * 추상체로 여러 옵션을 받아서 구현할 계획이지만 단순 시간을 나눠서 사용하는 기본구현체 부터 제공한다.
 * 관련 클래스는 클래스명 메소드명이 버전이 바뀌면 변경될 수 있다.
 * 수수료 계산이 필요하다
 * 상한가 로직은 계산되지 않음
 *
 * @author macle
 */
public class SplitBuy extends SplitOrder {



    protected BigDecimal buyCash = null;

    protected BigDecimal useCash = null;

    public SplitBuy(){

    }

    public void setBuyCash(BigDecimal buyCash) {
        this.buyCash = buyCash;
    }



    private BigDecimal remainderCash = BigDecimal.ZERO;

    public void buy(){

        valid();

        if(buyCash == null || buyCash.compareTo(BigDecimal.ZERO) <= 0){
            throw new RequiredFieldException("buy cash set error");
        }

        if(orderCase == null || (orderCase != OrderCase.MARKET && orderCase != OrderCase.BID)){
            throw new RequiredFieldException("order case set error (MARKET, BID), set order case: "  + orderCase);
        }

        BigDecimal buyCash = this.buyCash;

        //주문횟수

        BigDecimal avgBuyCash = buyCash.divide(new BigDecimal(orderCount), orderScale, RoundingMode.DOWN);

        for(;;){
            if(endTime <= System.currentTimeMillis()){
                break;
            }
            if(isStop){
                break;
            }

            try {

                BigDecimal orderPrice = getOrderPrice();

                //최소 주문 금액
                BigDecimal minOrderCash = orderPrice.multiply(getMinQuantity());

                BigDecimal orderCash = avgBuyCash.add(remainderCash);

                if(minOrderCash.compareTo(orderCash) > 0 ){
                    //최소 주문 금액 미만이면 restCash로 이동 (남은 금액)
                    remainderCash = orderCash;
                    continue;
                }
                //주문금액에 포함후 남은 금액 초기화
                remainderCash = BigDecimal.ZERO;
                trade(orderCash, orderPrice);
                Thread.sleep(delayTime);
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
    }

    public void trade(BigDecimal orderCash, BigDecimal orderPrice){
        if(orderCase == OrderCase.MARKET){

            MarketOrderTrade marketOrderTrade =  account.marketOrderCash(itemId, Trade.Type.BUY, orderCash);

            BigDecimal tradePrice = marketOrderTrade.getTradePrice().multiply(marketOrderTrade.getQuantity());
            quantitySum = quantitySum.add(marketOrderTrade.getQuantity());
            amountSum = amountSum.add(tradePrice);

            //주문후 남은금액
            BigDecimal orderRemainder = orderCash.subtract(tradePrice.add(marketOrderTrade.getFee()));
            if(orderRemainder.compareTo(BigDecimal.ZERO) > 0){
                remainderCash = remainderCash.add(orderRemainder);
            }

        }else if(orderCase == OrderCase.BID){
            //호가를 얻어와서
            //호가창을 가져오지 못한경우 현제 거래된 가격으로 매수주문

            LimitOrderCashTrade limitOrderCashTrade = account.limitOrderCash(itemId, Trade.Type.BUY, orderCash, orderPrice);

            addOpenOrder(limitOrderCashTrade);
            remainderCash = remainderCash.add(limitOrderCashTrade.getRemainderCash());
            updateOpenOrder();
        }
    }

    @Override
    public void openOrderCancel(LimitOrderTrade limitOrderTrade) {
        remainderCash = remainderCash.add(limitOrderTrade.getOpenQuantity().multiply(limitOrderTrade.getPrice()));
        //수수료 반환을 추가해야할지..
    }

}
