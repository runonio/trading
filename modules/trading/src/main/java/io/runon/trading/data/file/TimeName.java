package io.runon.trading.data.file;

import com.seomse.commons.utils.time.DateUtil;
import com.seomse.commons.utils.time.Times;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
        , HOUR_12
        , HOUR_6
        , HOUR_4
        , HOUR_3
        , HOUR_2
        , HOUR_1
    }

    static Type getDefaultType(long intervalTime){
        Type type;
        if(intervalTime >= Times.DAY_1){
            //100년
            type = Type.YEAR_100;

        }else if(intervalTime >= Times.HOUR_2){
            //20년
            type = Type.YEAR_20;

        }else if(intervalTime >= Times.HOUR_1){
            //10년
            type = Type.YEAR_10;

        }else if(intervalTime >= Times.MINUTE_5){
            //1년
            type = Type.YEAR_1;

        }else if(intervalTime >= Times.MINUTE_1){
            //1달
            type = Type.MONTH_1;

        }else if(intervalTime >= 5000L){
            type = Type.DAY_5;
            // 1 6 11 16 21 26
        }else if(intervalTime >= 2000L){
            type = Type.DAY_2;
            //2일
        }else if(intervalTime >= 1000L){
            //1일
            type = Type.DAY_1;
        }else{
            type = Type.HOUR_4;
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
        }else if(type == Type.HOUR_12){
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + DateUtil.getDateText(zonedDateTime.getHour() - zonedDateTime.getHour()%12);


        }else if(type == Type.HOUR_6){
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + DateUtil.getDateText(zonedDateTime.getHour() - zonedDateTime.getHour()%6);
        }else if(type == Type.HOUR_4){
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + DateUtil.getDateText(zonedDateTime.getHour() - zonedDateTime.getHour()%4);
        }else if(type == Type.HOUR_3){
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + DateUtil.getDateText(zonedDateTime.getHour() - zonedDateTime.getHour()%3);
        }else if(type == Type.HOUR_2){
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + DateUtil.getDateText(zonedDateTime.getHour() - zonedDateTime.getHour()%2);
        } else{
//           type == Type.HOUR_1
            //1시간
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + DateUtil.getDateText(zonedDateTime.getHour());
        }
    }

    static String [] getNames(long beginTime, long endTime , Type type, ZoneId zoneId){

        String beginName = getName(beginTime, type, zoneId);
        String endName = getName(endTime, type, zoneId);

        if(beginName.equals(endName)){
            return new String[]{beginName};
        }

        List<String> nameList = new ArrayList<>();
        nameList.add(beginName);
        
        String lastName = beginName;
        long lastTime = beginTime;
        long addTime = Times.HOUR_1;

        long endNameLong = Long.parseLong(endName);

        switch (type){

            case YEAR_100 -> {addTime = Times.DAY_1*360*100;
            }
            case YEAR_20 -> {addTime = Times.DAY_1*360*20;
            }
            case YEAR_10 -> {addTime = Times.DAY_1*360*10;
            }
            case YEAR_1 -> {addTime = Times.DAY_1*360;
            }
            case MONTH_1 -> {addTime = Times.DAY_1*20;
            }
            case DAY_5 -> {addTime = Times.DAY_5;
            }
            case DAY_2 -> {addTime = Times.DAY_2;
            }
            case DAY_1 -> {addTime = Times.DAY_1;
            }
            case HOUR_12 -> {addTime = Times.HOUR_12;
            }
            case HOUR_6 -> {addTime = Times.HOUR_6;
            }
            case HOUR_4 -> {addTime = Times.HOUR_4;
            }
            case HOUR_3 -> {addTime = Times.HOUR_3;
            }
            case HOUR_2 -> {addTime = Times.HOUR_2;
            }
            case HOUR_1 -> {
                //noinspection ReassignedVariable
                addTime = Times.HOUR_1;
            }
        }

        for(;;){
            lastTime += addTime;
            String nextName = getName(lastTime, type,zoneId);
            if(nextName.equals(lastName)){
                continue;
            }

            lastName = nextName;

            long nameLong = Long.parseLong(nextName);
            if(nameLong < endNameLong ){
                nameList.add(nextName);
            }else if(nameLong  == endNameLong){
                nameList.add(nextName);
                break;
            }else{
                break;
            }

        }

        String [] names = nameList.toArray(new String[0]);
        nameList.clear();
        return names;
    }

}
