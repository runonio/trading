package io.runon.trading;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author macle
 */
public interface TimeChangePercent extends Time {

    BigDecimal getChangePercent();

    BigDecimal getVolume();

    BigDecimal getAmount();


    public static BigDecimal getAvg(TimeChangePercent [] array, boolean isVolumeCheck){
        if(array == null || array.length ==0){
            return BigDecimal.ZERO;
        }

        int cnt = 0;

        BigDecimal sum = BigDecimal.ZERO;
        for(TimeChangePercent percent : array){
            if(isVolumeCheck){
                if(percent.getVolume().compareTo(BigDecimal.ZERO) == 0 && percent.getAmount().compareTo(BigDecimal.ZERO) == 0){
                    continue;
                }
            }
            sum = sum.add(percent.getChangePercent());
            cnt++;
        }

        if(cnt == 0){
            return  BigDecimal.ZERO;
        }

        return sum.divide(new BigDecimal(cnt), MathContext.DECIMAL128);
    }
}
