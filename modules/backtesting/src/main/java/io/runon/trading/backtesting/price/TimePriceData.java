package io.runon.trading.backtesting.price;

import io.runon.trading.TimePrice;

/**
 * 초단위 매매 백테스팅
 * @author macle
 */
public interface TimePriceData<E extends TimePrice> {

    void setData(E data);
}
