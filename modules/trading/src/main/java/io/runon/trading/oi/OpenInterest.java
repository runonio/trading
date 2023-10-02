package io.runon.trading.oi;

import io.runon.trading.Time;

import java.math.BigDecimal;

/**
 * 미체결약정
 * @author macle
 */
public interface OpenInterest extends Time {
    OpenInterest[] EMPTY_ARRAY = new OpenInterest[0];

    BigDecimal getOpenInterest();
    BigDecimal getNotionalValue();
}