package io.runon.trading;
/**
 * @author macle
 */
public interface BeginEndTime {

    BeginEndTime [] EMPTY_ARRAY = new BeginEndTime[0];

    long getBeginTime();

    long getEndTime();

}
