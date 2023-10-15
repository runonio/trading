package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 시장가 주문 체결 결과
 * @author macle
 */
public interface MarketOrderTrade {

    MarketOrderTrade EMPTY_MARKET_ORDER = new MarketOrderTradeEmpty();

    /**
     * 매매유형
     * @return 매매유형
     */
    Trade.Type getTradeType();

    /**
     * 체결가 얻기
     * 평단가
     * @return 실제 체결가격 ( 평단가 )
     */
    BigDecimal getTradePrice();

    /**
     * 체결 수량 얻기
     * @return 체결 수량
     */
    BigDecimal getQuantity();

    /**
     * 수수료 얻기
     * 총 매매에 사용한 금액은 체결가 * 수량 + 수수료
     * @return 수수료
     */
    BigDecimal getFee();

    /**
     * 매매전 확인가격
     * 최종 체결가
     * 슬리피지율을 계산하할떄 활용
     * @return 시장가 주문전 마지막 종가가
     */
    BigDecimal getLastClosePrice();

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
