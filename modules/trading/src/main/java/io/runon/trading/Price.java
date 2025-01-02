

package io.runon.trading;

import java.math.BigDecimal;

/**
 * @author macle
 */
public interface Price {

    /**
     * 종가 얻기
     * @return double 종가
     */
    BigDecimal getClose();
}
