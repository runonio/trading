package io.runon.trading.order;

import io.runon.trading.exception.RequiredFieldException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 분할매도
 * 매도할때는 수량만을 활용 (금액 버전은 필요가 있을때 이후개발)
 * 시장가 주문이 아닌 경우
 * 목포수량을 매도 하지 못할 수 있다.
 * @author macle
 */
@Slf4j
public class SplitSell extends SplitOrder{

    protected BigDecimal sellQuantity = null;

    public void setSellQuantity(BigDecimal sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    //매도
    public void sell(){
        valid();

        if(sellQuantity == null || sellQuantity.compareTo(BigDecimal.ZERO) <= 0){
            throw new RequiredFieldException("sell quantity set error");
        }


//        BigDecimal orderQuantity = buyCash.divide(new BigDecimal(orderCount), orderScale, RoundingMode.DOWN);

    }

    @Override
    public void openOrderCancel(LimitOrderTrade limitOrderTrade) {

    }

    public static void main(String[] args) {

    }


}