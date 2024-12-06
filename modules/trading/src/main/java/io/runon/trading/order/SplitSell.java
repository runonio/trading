package io.runon.trading.order;

import io.runon.commons.utils.ExceptionUtil;
import io.runon.trading.Trade;
import io.runon.trading.exception.RequiredFieldException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 분할매도
 * 매도할때는 수량만을 활용 (금액 버전은 필요가 있을때 이후개발)
 * 시장가 주문이 아닌 경우
 * 목포수량을 매도 하지 못할 수 있다.
 * 하한가 처리는 포함되지 않음
 * 
 * @author macle
 */
@Slf4j
public class SplitSell extends SplitOrder{

    protected BigDecimal sellQuantity = null;

    public void setSellQuantity(BigDecimal sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    private BigDecimal remainderQuantity = BigDecimal.ZERO;


    //매도
    public void sell(){
        valid();

        if(sellQuantity == null || sellQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new RequiredFieldException("sell quantity set error");
        }

        BigDecimal avgQuantity = sellQuantity.divide(new BigDecimal(orderCount), orderScale, RoundingMode.DOWN);

        for(;;) {
            if (endTime <= System.currentTimeMillis()) {
                break;
            }

            if(isStop){
                break;
            }
            
            try {
                BigDecimal orderQuantity = avgQuantity.add(remainderQuantity);
                BigDecimal minQuantity = getMinQuantity();

                if (minQuantity.compareTo(orderQuantity) > 0) {
                    remainderQuantity = orderQuantity;
                    continue;
                }

                remainderQuantity = BigDecimal.ZERO;
                BigDecimal remainder = orderQuantity.remainder(minQuantity);
                remainderQuantity = remainderQuantity.add(remainder);

                orderQuantity = orderQuantity.subtract(remainder);

                trade(orderQuantity);

                Thread.sleep(delayTime);
            }catch (Exception e){
                log.error(ExceptionUtil.getStackTrace(e));
                break;
            }
        }

    }

    public void trade(BigDecimal orderQuantity){
        if(orderCase == OrderCase.MARKET){

            MarketOrderTrade marketOrderTrade =  account.marketOrderQuantity(symbol, Trade.Type.BUY, orderQuantity);

            BigDecimal tradePriceSum = marketOrderTrade.getTradePrice().multiply(marketOrderTrade.getQuantity());
            quantitySum = quantitySum.add(marketOrderTrade.getQuantity());
            amountSum = amountSum.add(tradePriceSum);

        }else if(orderCase == OrderCase.ASK){

            LimitOrderTrade limitOrderTrade = account.limitOrderQuantity(symbol, Trade.Type.BUY, orderQuantity, getOrderPrice());
            addOpenOrder(limitOrderTrade);
            updateOpenOrder();
        }
    }

    @Override
    public void openOrderCancel(LimitOrderTrade limitOrderTrade) {
        remainderQuantity = remainderQuantity.add(limitOrderTrade.getOpenQuantity());
    }



}