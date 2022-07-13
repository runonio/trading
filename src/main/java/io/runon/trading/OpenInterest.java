package io.runon.trading;

import java.math.BigDecimal;

/**
 * 미체결약정
 * @author macle
 */
public interface OpenInterest extends Time{
    BigDecimal getOpenInterest();
    BigDecimal getNotionalValue();
}