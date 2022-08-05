package io.runon.trading.data.csv;

import com.seomse.commons.utils.time.DateUtil;
import com.seomse.commons.utils.time.Times;
import io.runon.trading.CandleTimes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * //    candle/symbol/1d/2000 = 100년 (36500)
 * //    candle/symbol/4h/2000 = 20년 (43800)
 * //    candle/symbol/3h/2000 = 20년 (58400)
 * //    candle/symbol/2h/2000 = 20년 (87600)
 * //    candle/symbol/1h/2000 = 10년 (87600)
 * //    candle/symbol/5m/2022 = 1년 (105000)
 * //    candle/symbol/1m/202207 = 1달 (43200)
 * //    candle/symbol/5s/20220701  = 5일 (86400)
 * //    candle/symbol/3s/20220701  = 2일 (57600)
 * //    candle/symbol/2s/20220701  = 2일 (86400)
 * //    candle/symbol/1s/20220707  = 1일 (86400)
 * csv 파일을 활용한 Trade 생성
 * @author macle
 */
public class CsvTimeName {

    //기본 타임 존은 뉴욕증시 시간으로 설정
    public static String getName(long time, long standardTime){
        return getName(time, standardTime, CandleTimes.US_STOCK_ZONE_ID);
    }

    public static String getName(long time, long standardTime, ZoneId zoneId){
        Instant i = Instant.ofEpochMilli(time);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(i, zoneId);
        if(standardTime >= Times.DAY_1){
            //100년
            return zonedDateTime.getYear()/100 + "00";
        }else if(standardTime >= Times.HOUR_2){
            //20년
            return Integer.toString(zonedDateTime.getYear() - zonedDateTime.getYear()%20);
        }else if(standardTime >= Times.HOUR_1){
            //10년
            return  zonedDateTime.getYear()/10 + "0";
        }else if(standardTime >= Times.MINUTE_5){
            //1년
            return Integer.toString(zonedDateTime.getYear());
        }else if(standardTime >= Times.MINUTE_1){
            //1달
            return zonedDateTime.getYear() +  DateUtil.getDateText(zonedDateTime.getMonthValue());
        }else if(standardTime >= 5000L){
            //5일
            // 1 6 11 16 21 26
            int day = zonedDateTime.getDayOfMonth() - (zonedDateTime.getDayOfMonth()-1) %5;
            if(day > 26){
                day = 26;
            }
            return zonedDateTime.getYear() +  DateUtil.getDateText(zonedDateTime.getMonthValue()) + DateUtil.getDateText(day );
        }else if(standardTime >= 2000L){
            //2일
            int day = zonedDateTime.getDayOfMonth() - (zonedDateTime.getDayOfMonth()-1)%2;
            if(day > 29){
                day = 29;
            }
            return zonedDateTime.getYear() +  DateUtil.getDateText(zonedDateTime.getMonthValue()) + DateUtil.getDateText(day );
        }else if(standardTime >= 1000L){
            //1일
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth());
        }else{
            return zonedDateTime.getYear() + DateUtil.getDateText(zonedDateTime.getMonthValue())
                    + DateUtil.getDateText(zonedDateTime.getDayOfMonth()) + DateUtil.getDateText(zonedDateTime.getHour());
        }

    }


    public static void main(String[] args) {
//        TimeZone zone = TimeZone.ad

//        ZoneId zoneId = ZoneId.of("US/Eastern");
//        System.out.println(zoneId.getId());

        System.out.println(getName(System.currentTimeMillis() , 1000, ZoneId.of("Asia/Seoul")));

    }
}
