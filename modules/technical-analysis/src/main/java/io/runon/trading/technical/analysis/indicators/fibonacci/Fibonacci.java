package io.runon.trading.technical.analysis.indicators.fibonacci;

import java.math.BigDecimal;

/**
 * @author macle
 */
public class Fibonacci {

    public static final BigDecimal N236 = new BigDecimal("0.236");
    public static final BigDecimal N382 = new BigDecimal("0.382");
    public static final BigDecimal N500 = new BigDecimal("0.5");
    public static final BigDecimal N618 = new BigDecimal("0.618");

    public static FibonacciData get(BigDecimal high, BigDecimal low){
        BigDecimal height = high.subtract(low);
        FibonacciData data = new FibonacciData();
        data.n000 = low;
        data.n1000 = high;
        if(height.compareTo(BigDecimal.ZERO) == 0){
            data.n236 = low;
            data.n382 = low;
            data.n500 = low;
            data.n618 = low;
            return data;
        }

        data.n236 = low.add(N236.multiply(height));
        data.n382 = low.add(N382.multiply(height));
        data.n500 = low.add(N500.multiply(height));
        data.n618 = low.add(N618.multiply(height));
        return data;
    }




}
