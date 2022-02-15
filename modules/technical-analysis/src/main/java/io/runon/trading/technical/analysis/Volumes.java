package io.runon.trading.technical.analysis;

import io.runon.trading.BigDecimals;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 거래량 관련 유틸성 클래스
 * @author macle
 */
public class Volumes {

    //100.0 == 100% , 500.0 == 500%
    public static final BigDecimal MAX_VOLUME_POWER = new BigDecimal(500);

    /**
     * 체결강도
     * @return 체결강도 얻기
     */
    public static BigDecimal getVolumePower(BigDecimal buyVolume, BigDecimal sellVolume){
        if(sellVolume.compareTo(buyVolume) == 0){
            return BigDecimals.DECIMAL_100;
        }

        if( sellVolume.compareTo(BigDecimal.ZERO) == 0){
            //500%
            return MAX_VOLUME_POWER;
        }

        BigDecimal strength = buyVolume.divide(sellVolume, MathContext.DECIMAL128).multiply(BigDecimals.DECIMAL_100);
        if(strength.compareTo(MAX_VOLUME_POWER) > 0){
            return MAX_VOLUME_POWER;
        }

        return strength;
    }

}
