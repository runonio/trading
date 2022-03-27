package io.runon.trading;

import java.math.BigDecimal;

/**
 * 매매금액
 * @author macle
 */
public interface TradingPrice {
    /**
     * 매매금액얻기
     * @return 매매금액 (매도 혹은 매수 금액)
     */
    BigDecimal getTradingPrice();
}
