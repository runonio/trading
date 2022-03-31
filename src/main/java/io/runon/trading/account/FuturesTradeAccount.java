package io.runon.trading.account;

import io.runon.trading.Trade;
import io.runon.trading.order.MarketPriceOrder;

import java.math.BigDecimal;

/**
 * 선물 매매 계좌 (실제계좌)
 * @author macle
 */
public interface FuturesTradeAccount extends FuturesAccount{

    /**
     * 현금사용
     * 수량활용
     * @param symbol 심볼 ( 종목 아이디)
     * @param type BUY, SELL
     * @param quantity 수량
     * @return 시장가 주문정보
     */
    MarketPriceOrder orderQuantity(String symbol, Trade.Type type, BigDecimal quantity);

    /**
     * 현금사용
     * 시장가주문
     * @param symbol 심볼 ( 종목 아이디)
     * @param type BUY, SELL
     * @param cash 현금, 달러 혹은 원화 (거래소 기준 금액)
     * @return 시장가 주문정보
     */
    MarketPriceOrder orderCash(String symbol, Trade.Type type, BigDecimal cash);


    /**
     * 포지션 종료 (시장가)
     * @param symbol 심볼 ( 종목 아이디)
     * @return 시장가 종료정보
     */
    MarketPriceOrder closePosition(String symbol);

}
