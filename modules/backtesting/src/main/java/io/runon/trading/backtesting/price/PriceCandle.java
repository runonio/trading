package io.runon.trading.backtesting.price;

import io.runon.trading.Candle;
/**
 * 백테스팅에서 사용하는 가격용 캔들
 * @author macle
 */
public interface PriceCandle {

    /**
     * 유효성
     * 정검으로 인한 시간은 continue 하게 한다
     * @param time 현재시간
     * @return 점검등으로 데이터가 비어있는경우 false
     */
    boolean isValid(long time);

    Candle getPriceCandle();
}
