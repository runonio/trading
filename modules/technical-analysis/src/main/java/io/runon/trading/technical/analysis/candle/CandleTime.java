package io.runon.trading.technical.analysis.candle;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 캔들 시간 관련
 * @author macle
 */
public class CandleTime {

    /**
     * 년원일시분
     *
     * @param time unix time
     * @param zoneId example ZoneId.of("Asia/Seoul")
     * @return yyyyMMdd HHmm
     */
    public static String ymdhm(long time, ZoneId zoneId){
        Instant i = Instant.ofEpochMilli(time);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(i, zoneId);

        return zonedDateTime.getYear() + getText(zonedDateTime.getMonthValue()) + getText(zonedDateTime.getDayOfMonth()) + " " + getText(zonedDateTime.getHour()) + getText(zonedDateTime.getMinute()) ;
    }

    public static String getText(int value){
        if(value < 10){
            return "0" + value;
        }
        return Integer.toString(value);
    }

}
