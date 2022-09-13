package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 지정가 주문 매매 정보
 * @author macle
 */
public interface LimitOrderTrade {

    enum Type{
        OPEN, //미체결
        CLOSE // 체결
    }

    /**
     * 체결 정보 얻기
     * @return 체결 또는 미체결 (OPEN, CLOSE)
     */
    Type getType();

    /**
     * 매매유형
     * @return 매매유형
     */
    Trade.Type getTradeType();

    /**
     * 가격
     * @return 지정가
     */
    BigDecimal getLimitPrice();

    /**
     * 수량 얻기
     * @return 수량
     */
    BigDecimal getQuantity();

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
