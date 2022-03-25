package io.runon.trading.backtesting;

import io.runon.trading.Price;
/**
 * 시간과 가격정보
 * @author macle
 */
public interface TimePrice extends Price {
    long getTime();
}
