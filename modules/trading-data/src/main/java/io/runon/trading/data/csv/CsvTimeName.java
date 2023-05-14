package io.runon.trading.data.csv;

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
        TimeName.Type type = TimeName.getCandleType(intervalTime);
        return TimeName.getName(time,type,zoneId);
    }

}
