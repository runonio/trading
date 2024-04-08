package io.runon.trading.technical.analysis.candle;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import lombok.Data;

/**
 * 시간과 캔들값
 * @author macle
 */
@Data
public class TimeCandle {

    private long time;
    private TradeCandle candle;

    public TimeCandle(){}

    public TimeCandle(long time, TradeCandle candle){
        this.time = time;
        this.candle = candle;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }

}
