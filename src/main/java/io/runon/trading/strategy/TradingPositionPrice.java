package io.runon.trading.strategy;

import io.runon.trading.TradingPrice;

/**
 * 매매 유형과 금액(매수 매도 금액)
 * @author macle
 */
public interface TradingPositionPrice extends TradingPrice {
    Position getPosition();
}
