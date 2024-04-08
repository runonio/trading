package io.runon.trading.data.file;

import io.runon.trading.TradingTimes;

import java.time.ZoneId;

/**
 * TimeName 기본형
 * @author macle
 */
public class TimeNameImpl implements TimeName{

    private ZoneId zoneId = TradingTimes.UTC_ZONE_ID;
    private final Type type;
    public TimeNameImpl(Type type){
        this.type = type;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String getName(long time) {
        return TimeName.getName(time, type, zoneId);
    }
}
