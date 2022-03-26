package io.runon.trading.backtesting;

import io.runon.trading.backtesting.price.TimePrice;

/**
 * 실시간 매매 백테스팅
 * @author macle
 */
public abstract class FuturesRealtimeBacktesting<E extends TimePrice> extends FuturesBacktesting<E> {

    public void putData(E data){

    }

}