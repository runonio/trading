package io.runon.trading;

import java.math.BigDecimal;

/**
 * 미체결약정
 * @author macle
 */
public interface OpenInterest {
    long getTime();
    BigDecimal getOpenInterest();
    BigDecimal getNotionalValue();
}