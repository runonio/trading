package io.runon.trading.data;

import lombok.Data;

/**
 * @author macle
 */
@Data
public class TimeRange {
    long beginTime;
    long endTime;

    public TimeRange(){

    }

    public TimeRange(long beginTime, long endTime){
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
}
