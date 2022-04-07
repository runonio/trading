package io.runon.trading;

import com.seomse.commons.utils.time.Times;

import java.util.Calendar;

/**
 * @author macle
 */
public class CandleTimes {

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
    /**
     * 처음 시작할때의 시작시간 얻기
     * @param standardTime 기준시간 1분 1시간 1일
     * @param time 시간 (밀리초)
     * @return openTime
     */
    public static long getOpenTime(long standardTime, long time){

        Calendar calendar = Calendar.getInstance();

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
}
