package io.runon.trading.data.file;

import io.runon.trading.TradingTimes;

import java.time.ZoneId;

/**
 * TimeName 기본형
 * @author macle
 */
public class TimeNameImpl implements TimeName{

    private final Type type;
    public TimeNameImpl(Type type){
        this.type = type;
    }



    @Override
    public String getName(long time) {
        return TimeName.getName(time, type);
    }
}
