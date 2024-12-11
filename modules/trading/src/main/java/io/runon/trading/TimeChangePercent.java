package io.runon.trading;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author macle
 */
public interface TimeChangePercent extends Time {

    BigDecimal getChangePercent();

    public static BigDecimal getAvg(TimeChangePercent [] array){
        if(array == null || array.length ==0){
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for(TimeChangePercent percent : array){
            sum = sum.add(percent.getChangePercent());
        }
        return sum.divide(new BigDecimal(array.length), MathContext.DECIMAL128);
    }
}
