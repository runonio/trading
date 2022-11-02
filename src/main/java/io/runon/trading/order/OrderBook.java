package io.runon.trading.order;

import io.runon.trading.PriceQuantity;

/**
 * 호가창
 * @author macle
 */
public interface OrderBook {
    /**
     * 호가창 시간
     * @return 시간얻기
     */
    long getTime();

    /**
     * 매수호가 목록 얻기
     * @return 매수호가 목록
     */
    PriceQuantity[] getAsks();

    /**
     * 매도호가 목록얻기
     * @return 매도호가 목록
     */
    PriceQuantity[] getBids();
}
