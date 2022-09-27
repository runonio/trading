package io.runon.trading;

import java.math.BigDecimal;

/**
 * 시간과 숫자
 * @author macle
 */
public interface TimeNumber {
    TimeNumber[] EMPTY_ARRAY = new TimeNumber[0];

    long getTime();
    BigDecimal getNumber();
}
