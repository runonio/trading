package io.runon.trading.data.file;

import com.seomse.commons.utils.time.DateUtil;
import com.seomse.commons.utils.time.Times;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * @author macle
 */
public interface TimeName {

    String getName(long time);

    enum Type{
        YEAR_100
        , YEAR_20
        , YEAR_10
        , YEAR_1
        , MONTH_1
        , DAY_5
        , DAY_2
        , DAY_1
        , HOUR_1
    }


    static Type getCandleType(long intervalTime){
        TimeName.Type type;
        if(intervalTime >= Times.DAY_1){
            //100년
            type = TimeName.Type.YEAR_100;

        }else if(intervalTime >= Times.HOUR_2){
            //20년
            type = TimeName.Type.YEAR_20;

        }else if(intervalTime >= Times.HOUR_1){
            //10년
            type = TimeName.Type.YEAR_10;

        }else if(intervalTime >= Times.MINUTE_5){
            //1년
            type = TimeName.Type.YEAR_1;

        }else if(intervalTime >= Times.MINUTE_1){
            //1달
            type = TimeName.Type.MONTH_1;

        }else if(intervalTime >= 5000L){
            type = TimeName.Type.DAY_5;
            // 1 6 11 16 21 26
        }else if(intervalTime >= 2000L){
            type = TimeName.Type.DAY_2;
            //2일
        }else if(intervalTime >= 1000L){
            //1일
            type = TimeName.Type.DAY_1;

        }else{
            type = TimeName.Type.HOUR_1;
        }
        return type;
    }

    static String getName(long time, Type type, ZoneId zoneId){
        Instant i = Instant.ofEpochMilli(time);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(i, zoneId);
        if(type == Type.YEAR_100){
            //100년
            return zonedDateTime.getYear()/100 + "00";
        }else if(type == Type.YEAR_20){
            //20년
            return Integer.toString(zonedDateTime.getYear() - zonedDateTime.getYear()%20);
        }else if(type == Type.YEAR_10){
            //10년
            return  zonedDateTime.getYear()/10 + "0";
        }else if(type == Type.YEAR_1){
            //1년
            return Integer.toString(zonedDateTime.getYear());
        }else if(type == Type.MONTH_1){
            //1달
            return zonedDateTime.getYear() +  DateUtil.getDateText(zonedDateTime.getMonthValue());
        }else if(type == Type.DAY_5){
            //5일
            // 1 6 11 16 21 26
            int day = zonedDateTime.getDayOfMonth() - (zonedDateTime.getDayOfMonth()-1) %5;
            if(day > 26){
                day = 26;
            }
            return zonedDateTime.getYear() +  DateUtil.getDateText(zonedDateTime.getMonthValue()) + DateUtil.getDateText(day );
        }else if(type == Type.DAY_2){
            //2일
            int day = zonedDateTime.getDayOfMonth() - (zonedDateTime.getDayOfMonth()-1)%2;
            if(day > 29){
                day = 29;
            }
            return zonedDateTime.getYear() +  DateUtil.getDateText(zonedDateTime.getMonthValue()) + DateUtil.getDateText(day );
        }else if(type == Type.DAY_1){
            //1일
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth());
        }else{
//           type == Type.HOUR_1
            //1시간
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + DateUtil.getDateText(zonedDateTime.getHour());
        }

    }
}
