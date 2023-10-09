package io.runon.trading.order;

import io.runon.trading.Trade;

import java.math.BigDecimal;

/**
 * 시장가 주문
 * @author macle
 */
public interface MarketOrder {

    /**
     * 현금사용
     * 수량활용
     * @param symbol 심볼 ( 종목 아이디)
     * @param type BUY, SELL
     * @param quantity 수량
     * @return 시장가 주문정보
     */
    MarketOrderTrade marketOrderQuantity(String symbol, Trade.Type type, BigDecimal quantity);

    /**
     * 현금사용
     * 시장가주문
     * @param symbol 심볼 ( 종목 아이디)
     * @param type BUY, SELL
     * @param cash 현금, 달러 혹은 원화 (거래소 기준 금액)
     * @return 시장가 주문정보
     */
    MarketOrderTrade marketOrderCash(String symbol, Trade.Type type, BigDecimal cash);


    /**
     * 포지션 종료 (시장가)
     * @param symbol 심볼 ( 종목 아이디)
     * @return 시장가 종료정보
     */
    MarketOrderTrade closePosition(String symbol);
}
