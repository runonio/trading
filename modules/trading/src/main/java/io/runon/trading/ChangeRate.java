

package io.runon.trading;

import java.math.BigDecimal;

/**
 * 가격 변화율
 * 가격 변화율만 활용하는 패턴이 있음
 * @author macle
 */
public interface ChangeRate {
    /**
     * previous 기준
     * (일별이면 전 거래일, 분봉이면 전봉))
     * @return 변동율
     */
    BigDecimal getChangeRate();
}