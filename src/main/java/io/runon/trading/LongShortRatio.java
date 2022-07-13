package io.runon.trading;

import java.math.BigDecimal;

/**
 * 롱숏비율
 * @author macle
 */
public interface LongShortRatio {

    long getTime();
    BigDecimal getLongAccount();
    BigDecimal getShortAccount();

    /**
     * long short ratio
     * @return short account / long account
     */
    BigDecimal getRatio();

}
