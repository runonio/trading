package io.runon.trading.oi;

import java.math.BigDecimal;

/**
 * 메치결 약정 유틸성 메소드
 * @author macle
 */
public class OpenInterests {
    public static void sum(OpenInterestData data, OpenInterest sum){
        BigDecimal openInterest = sum.getOpenInterest();
        if(openInterest != null){
            data.openInterest = data.openInterest.add(openInterest);
        }

        BigDecimal notionalValue = sum.getNotionalValue();
        if(notionalValue != null){
            data.notionalValue = data.notionalValue.add(notionalValue);
        }

    }
}
