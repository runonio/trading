package io.runon.trading.technical.analysis.indicators.fibonacci;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class FibonacciData {
    long time;

    long highTime;
    long lowTime;
    BigDecimal n000;
    BigDecimal n236;
    BigDecimal n382;
    BigDecimal n500;
    BigDecimal n618;
    BigDecimal n1000;
}
