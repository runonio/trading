package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 지정가 주문
 * @author macle
 */
public interface LimitOrder {
    /**
     * 현금사용
     * 수량활용
     * @param symbol 심볼 ( 종목 아이디)
     * @param type BUY, SELL
     * @param quantity 수량
     * @param limitPrice 지정가격
     * @return 지정가 매매 정보 (체결 미체결)
     */
    LimitOrderTrade limitOrderQuantity(String symbol, Trade.Type type, BigDecimal quantity, BigDecimal limitPrice);

    /**
     * 현금사용
     * 지정가주문
     * @param symbol 심볼 ( 종목 아이디)
     * @param type BUY, SELL
     * @param cash 현금, 달러 혹은 원화 (거래소 기준 금액)
     * @param limitPrice 지정가격
     * @return 지정가 매매 정보 (체결 미체결)
     */
    LimitOrderTrade limitOrderCash(String symbol, Trade.Type type, BigDecimal cash, BigDecimal limitPrice);

    /**
     * 전체 현금으로 계산하여 매수 혹은 매도한다.
     * @param symbol 심볼 ( 종목 아이디)
     * @param type BUY, SELL
     * @param cash 총현금
     * @param beginPrice 시작 지정가
     * @param endPrice 끝 지정가
     * @param priceGap 가격 갭
     * @return 지정가 매매 정보 (체결 미체결)
     */
    LimitOrderTrade limitOrderCash(String symbol, Trade.Type type, BigDecimal cash, BigDecimal beginPrice, BigDecimal endPrice, BigDecimal priceGap );



}
