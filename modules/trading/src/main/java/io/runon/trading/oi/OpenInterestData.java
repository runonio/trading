package io.runon.trading.oi;

import io.runon.commons.utils.GsonUtils;

import java.math.BigDecimal;

/**
 * 미체결약정 구현체
 * @author macle
 */
public class OpenInterestData implements OpenInterest {
    long time;
    BigDecimal openInterest;
    BigDecimal notionalValue;

    public OpenInterestData(){

    }

    public OpenInterestData(long time, BigDecimal openInterest){
        this.time = time;
        this.openInterest = openInterest;
    }

    public OpenInterestData(long time, BigDecimal openInterest, BigDecimal notionalValue){
        this.time = time;
        this.openInterest = openInterest;
        this.notionalValue = notionalValue;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setOpenInterest(BigDecimal openInterest) {
        this.openInterest = openInterest;
    }

    public void setNotionalValue(BigDecimal notionalValue) {
        this.notionalValue = notionalValue;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public BigDecimal getOpenInterest() {
        return openInterest;
    }

    @Override
    public BigDecimal getNotionalValue() {
        return notionalValue;
    }

    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }


}
