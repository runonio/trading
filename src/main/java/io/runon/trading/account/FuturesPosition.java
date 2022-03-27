package io.runon.trading.account;

import io.runon.trading.TradingPrice;
import io.runon.trading.strategy.Position;

import java.math.BigDecimal;

/**
 * 선물 포지션
 * @author macle
 */
public interface FuturesPosition extends TradingPrice {
    /***
     * 심볼얻기
     * @return 심볼
     */
    String getSymbol();

    /**
     * 가격얻기
     * @return 평단가
     */
    BigDecimal getPrice();

    /**
     * 수량 얻기
     * @return 수량
     */
    BigDecimal getQuantity();

    /**
     * 레버리지
     * @return 레버리지
     */
    BigDecimal getLeverage();

    /**
     * 포지션 LONG(BUY), SHORT(sell)
     * @return LONG(BUY), SHORT(sell)
     */
    Position getPosition();
}
