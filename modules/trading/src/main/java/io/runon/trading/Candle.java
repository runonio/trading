
package io.runon.trading;

import java.math.BigDecimal;

/**
 * @author macle
 */
public interface Candle extends Price {

    /**
     * 시가 얻기
     * @return 시가 (시작가)
     */
    BigDecimal getOpen();

    /**
     * 고가 얻기
     * @return 고가
     */
    BigDecimal getHigh();
    /**
     * 저가 얻기
     * @return 저가
     */
    BigDecimal getLow();
}
