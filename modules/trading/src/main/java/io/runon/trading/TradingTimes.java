package io.runon.trading;

import com.seomse.commons.exception.UndefinedException;
import com.seomse.commons.utils.string.Check;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author macle
 */
public class TradingTimes {

    //기본 타임 존은 뉴욕증시 시간으로 설정 (동부 표준시)
    //America/New_York == US/Eastern 서머타임에는 1시간 빠른시간을 활용
    public static final ZoneId USA_ZONE_ID = ZoneId.of("America/New_York");
    public static final ZoneId KOR_ZONE_ID = ZoneId.of("Asia/Seoul");
    public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    public static final ZoneId SGP_ZONE_ID = ZoneId.of("Asia/Singapore");

    public static final ZoneId INR_ZONE_ID = ZoneId.of("Asia/Kolkata");

    public static ZoneId getZoneId(String countryCode){
        return getZoneId(CountryCode.valueOf(countryCode.toUpperCase()));
    }

    public static ZoneId getZoneId(CountryCode countryCode){
        switch (countryCode){
            case KOR -> {
                return KOR_ZONE_ID;
            }
            case USA -> {
                return USA_ZONE_ID;
            }
            case SGP -> {
                return SGP_ZONE_ID;
            }
            case INR -> {
               return INR_ZONE_ID;
            }
            default -> throw new UndefinedException();
        }
    }

    public static String getInterval(long time){
        if(time >= Times.WEEK_1 && time%Times.WEEK_1 == 0 ) {
            return time/Times.WEEK_1 + "w";
        }else if(time >= Times.DAY_1){
            return time/Times.DAY_1 + "d";
        }else if(time >= Times.HOUR_1){
            return time/Times.HOUR_1 + "h";
        }else if(time >= Times.MINUTE_1){
            return time/Times.MINUTE_1 + "m";
        }else{
            return time/1000L + "s";
        }
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

    public static boolean isInterval(String interval){
        char timeUnit = interval.charAt(interval.length()-1);

        if(
                timeUnit != 'm'
                && timeUnit != 'h'
                && timeUnit != 'd'
                && timeUnit != 's'
                && timeUnit != 'w'
                && timeUnit != 'M'
        ){
            return false;
        }

        String timeNumber = interval.substring(0, interval.length()-1);
        return Check.isNumber(timeNumber);
    }


    public static long getOpenTime(long standardTime, long time){
        return getOpenTime(standardTime, time, null);
    }

    /**
     * 처음 시작할때의 시작시간 얻기
     * @param standardTime 기준시간 1분 1시간 1일
     * @param time 시간 (밀리초)
     * @return openTime
     */
    public static long getOpenTime(long standardTime, long time, ZoneId zoneId){

        Calendar calendar = Calendar.getInstance();
        if(zoneId != null) {
            calendar.setTimeZone(TimeZone.getTimeZone(zoneId));
        }
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(standardTime < Times.DAY_1){
            long gap = time - calendar.getTimeInMillis();
            return calendar.getTimeInMillis() + gap - gap%standardTime;
        }else{
            //이건 주봉 월봉 년봉 쓰기시작하면 하기
            //우선은 일단위 봉생성 타임만 저장
            //주
//            calendar.set(Calendar.DAY_OF_WEEK, 0);
            //월
//            calendar.set(Calendar.DAY_OF_WEEK, 0);
            //년
//            calendar.set(Calendar.DAY_OF_YEAR, 1);
            return calendar.getTimeInMillis();
        }
    }

    public static YearQuarter nextYearQuarter(YearQuarter yearQuarter){
        return nextYearQuarter(yearQuarter.getYear(), yearQuarter.getQuarter());
    }

    public static YearQuarter nextYearQuarter(int year, int quarter){

        quarter++;
        if(quarter > 4){
            year++;
            quarter =1;
        }
        return new YearQuarter(year, quarter);
    }

    public static YearQuarter nowYearQuarter(ZoneId zoneId){

        Instant i = Instant.ofEpochMilli(System.currentTimeMillis());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(i, zoneId);

        int year = zonedDateTime.getYear();
        int quarter;

        int month = zonedDateTime.getMonthValue();
        if(month < 4){
            quarter = 1;
        }else if( month < 7){
            quarter = 2;
        }else if( month < 10){
            quarter = 3;
        }else{
            quarter = 4;
        }

        return new YearQuarter(year, quarter);
    }


    public static List<YearQuarter> getYearQuarterList(YearQuarter begin, ZoneId zoneId){
        YearQuarter now = nowYearQuarter(zoneId);

        if(begin.compareTo(now) > 0){
            return Collections.emptyList();
        }

        List<YearQuarter> yearQuarterList = new ArrayList<>();

        YearQuarter yearQuarter = begin;

        //noinspection ConditionalBreakInInfiniteLoop
        for(;;){
            yearQuarterList.add(yearQuarter);

            yearQuarter = nextYearQuarter(yearQuarter);

            if(yearQuarter.compareTo(now) > 0){
               break;
            }
        }
        return yearQuarterList;
    }


    public static YearQuarter [] getYearQuarters(YearQuarter begin, ZoneId zoneId){
        List<YearQuarter> list = getYearQuarterList(begin,zoneId);
        if(list.size() == 0){
            return YearQuarter.EMPTY_ARRAY;
        }

        YearQuarter [] array = list.toArray(new YearQuarter[0]);
        list.clear();

        return array;
    }


    public static long getDailyOpenTime( CountryCode countryCode, String ymd){

        if(countryCode == CountryCode.KOR){
            ZoneId zoneId = TradingTimes.getZoneId(countryCode);
            long time = YmdUtil.getTime(ymd, zoneId);
            return time + Times.getTimeHm("0900");
        }else{
            throw new UndefinedException("undefined code: " + countryCode.toString());
        }
    }


}
