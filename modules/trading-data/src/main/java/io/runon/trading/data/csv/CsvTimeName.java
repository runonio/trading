package io.runon.trading.data.csv;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.TimeName;

import java.time.ZoneId;

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



    public static String getPath(String dirPath, String symbol, long intervalTime, ZoneId zoneId, long time){
        return dirPath +"/" + symbol +"/" + TradingTimes.getInterval(intervalTime) + "/" + getName(time, intervalTime, zoneId);
    }

    public static String getPath(String dirPath, String symbol, long intervalTime, ZoneId zoneId, String interval, long time){
        return dirPath +"/" + symbol +"/" + interval + "/" + getName(time, intervalTime, zoneId);
    }

    //기본 타임 존은 UTC로 설정
    public static String getName(long time, long intervalTime){
        return getName(time, intervalTime, TradingTimes.UTC_ZONE_ID);
    }

    public static String getName(long time, long intervalTime, ZoneId zoneId){
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
        return TimeName.getName(time,type,zoneId);
    }

}
