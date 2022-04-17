package io.runon.trading;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 시간과 숫자
 * @author macle
 */
@Data
public class TimeNumberData implements TimePrice, TimeNumber {

    long time;
    BigDecimal number;

    public TimeNumberData(){

    }

    public TimeNumberData(long time, BigDecimal number){
        this.time = time;
        this.number = number;
    }


    @Override
    public BigDecimal getClose() {
        return number;
    }
}
