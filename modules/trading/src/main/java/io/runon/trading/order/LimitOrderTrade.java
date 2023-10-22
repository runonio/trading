package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 지정가 주문 매매 정보
 * @author macle
 */
public interface LimitOrderTrade {

    //주문 아이디 얻기
    String getOrderId();

    /**
     * 매매유형
     * @return 매매유형
     */
    Trade.Type getTradeType();

    /**
     * 가격
     * @return 지정가
     */
    BigDecimal getPrice();

    /**
     * 미체결 수량 얻기
     * @return 수량
     */
    BigDecimal getOpenQuantity();

    /**
     * 체결 수량 얻기
     * @return 수량
     */
    BigDecimal getCloseQuantity();

    /**
     * 수수료 얻기
     * @return 누적수수료
     */
    BigDecimal getFee();

    /**
     * 주문 시간 얻기
     * @return 주문시간
     */
    long getOrderTime();

    /**
     * 체결 시간 혹은 취소시간
     * @return 체결시간 혹은 취소시간
     */
    long getCloseTime();

}
