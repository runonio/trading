package io.runon.trading.technical.analysis.similarity;

import io.runon.trading.Time;
import io.runon.trading.TradingMath;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author macle
 */
public interface SimChangeData extends Time {

    BigDecimal getChangePercent();

    BigDecimal getVolume();

    BigDecimal getAmount();

    public static BigDecimal getAvg(SimChangeData[] array, boolean isVolumeCheck){
        if(array == null || array.length ==0){
            return BigDecimal.ZERO;
        }

        int cnt = 0;

        BigDecimal sum = BigDecimal.ZERO;
        for(SimChangeData percent : array){
            if(isVolumeCheck){
                if(percent.getVolume().compareTo(BigDecimal.ZERO) == 0 && percent.getAmount().compareTo(BigDecimal.ZERO) == 0){
                    continue;
                }
            }

            BigDecimal num = percent.getChangePercent();
            if(num.compareTo(BigDecimal.ZERO) < 0){
                sum = sum.subtract(TradingMath.recoveryPercent(num));
            }else{
                sum = sum.add(num);
            }

            cnt++;
        }

        if(cnt == 0){
            return  BigDecimal.ZERO;
        }

        return sum.divide(new BigDecimal(cnt), MathContext.DECIMAL128);
    }
}
