package io.runon.trading.technical.analysis.candle;

import com.seomse.commons.utils.time.DateUtil;
import com.seomse.commons.utils.time.Times;

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

        return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue()) + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + " " + DateUtil.getDateText(zonedDateTime.getHour()) + DateUtil.getDateText(zonedDateTime.getMinute()) ;
    }

    public static long getIntervalTime(String interval){
        char timeUnit = interval.charAt(interval.length()-1);
        String timeNumber = interval.substring(0, interval.length()-1);
        
        long time;
        
        if(timeUnit == 'm'){
            time = Times.MINUTE_1 * Long.parseLong(timeNumber);
        }else if(timeUnit == 'h'){
            time = Times.HOUR_1 * Long.parseLong(timeNumber);
        }else if(timeUnit == 'd'){
            time = Times.DAY_1 * Long.parseLong(timeNumber);
        }else if(timeUnit == 's') {
            //초데이터는 잘 사용하지않기 때문에
            time = 1000 * Long.parseLong(timeNumber);
        }else if(timeUnit == 'w'){
            time = Times.WEEK_1 * Long.parseLong(timeNumber);
        }else if(timeUnit == 'M'){
            //월 단위를 체크하는 기능을 만들고 지원해야함
            //1달부터는 지원하지 않음
            throw new IllegalArgumentException("interval error: " + interval);
        }else{
            throw new IllegalArgumentException("interval error: " + interval);
        }
                
        return time;
        
    }
    
    

}
