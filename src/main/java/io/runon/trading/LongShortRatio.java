package io.runon.trading;

import java.math.BigDecimal;

/**
 * 롱숏비율
 * @author macle
 */
public interface LongShortRatio extends Time{

    BigDecimal getLongAccount();
    BigDecimal getShortAccount();

    /**
     * long short ratio
     * @return long account / short account
     */
    BigDecimal getRatio();

}
