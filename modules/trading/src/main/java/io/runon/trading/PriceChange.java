
package io.runon.trading;

import java.math.BigDecimal;

/**
 * 가격과 가격 변화
 * @author macle
 */
public interface PriceChange extends ChangeRate, Price{

    /**
     * previous 기준
     * (일별이면 전 거래일, 분봉이면 전봉))
     * @return 변동가격
     */
    BigDecimal getChange();

    /**
     * (일별이면 전 거래일, 분봉이면 전봉)
     * @return 전 거래 가격
     */
    BigDecimal getPrevious();
}
