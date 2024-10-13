package io.runon.trading.data.daily;

import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.trading.Time;
import io.runon.trading.TradingGson;
import io.runon.trading.data.json.JsonOutLine;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZoneId;

/**
 * 일별 숫자형 데이터 정보
 * @author macle
 */
@Data
public class DailyNumber implements Time , JsonOutLine {

    Long t;

    int ymd;

    BigDecimal number;

    public DailyNumber(){
    }

    public DailyNumber(long time, int ymd, BigDecimal number){
        this.t = time;
        this.ymd = ymd;
        this.number = number;
    }

    public DailyNumber(long time, BigDecimal number, ZoneId zoneId){
        this.t = time;
        this.number = number;
        this. ymd =Integer.parseInt(YmdUtil.getYmd(time, zoneId));
    }


    public DailyNumber(String hhmmss, int ymd, BigDecimal number, ZoneId zoneId){


        this.t = Times.getTime("yyyyMMdd hhmmss", ymd + " " + hhmmss, zoneId);
        this.number = number;
        this.ymd = ymd;
    }
    public DailyNumber(int ymd, BigDecimal number, ZoneId zoneId) {
        this.t = YmdUtil.getTime(ymd, zoneId );
        this.number = number;
        this.ymd = ymd;
    }


        public static DailyNumber make(String jsonStr){

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, DailyNumber.class);
    }


    @Override
    public String outTimeLineJsonText(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }



    @Override
    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }

}
