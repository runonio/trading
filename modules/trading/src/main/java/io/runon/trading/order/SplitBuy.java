package io.runon.trading.order;

import io.runon.commons.utils.GsonUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 분할매수, 심플하게 재구현
 * 전략에서 관리하는 방식으로 변경
 * 관련 클래스는 초기 세팅금액과, 얼마나 남았느지를 관리하는 정보성 클래스로 대체한다.
 * @author macle
 */
@Data
public class SplitBuy {
    //총 주문금액
    private BigDecimal planCash;
    
    //분할 건수
    private int planCount;

    //체결금액
    private BigDecimal buyCash;

    //체결 건수
    private int tradeCount;

    //마지막 체결시간
    private Long lastTradeTime;

    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

}