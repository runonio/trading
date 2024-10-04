package io.runon.trading.data.daily;

import io.runon.trading.Time;
import io.runon.trading.TradingGson;
import io.runon.trading.data.json.JsonOutLine;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 체결데이터 일별
 * (한국 주식 데이터 기준정보)
 * @author macle
 */
@Data
public class VolumePowerDaily implements  Time, JsonOutLine {

    public static final VolumePowerDaily [] EMPTY_ARRAY = new VolumePowerDaily[0];
    public static final Comparator<VolumePowerDaily> SORT = Comparator.comparingInt(o -> o.ymd);

    Long t ;

    int ymd;


    BigDecimal buyVolume;
    BigDecimal sellVolume;

    public static VolumePowerDaily make(String jsonStr){

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, VolumePowerDaily.class);
    }


    @Override
    public String outTimeLineJsonText(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }


    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }
}
