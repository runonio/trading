package io.runon.trading;

import java.math.BigDecimal;

/**
 * 시간과 숫자
 * @author macle
 */
public interface TimeNumber extends Time{

    BigDecimal getNumber();
}
