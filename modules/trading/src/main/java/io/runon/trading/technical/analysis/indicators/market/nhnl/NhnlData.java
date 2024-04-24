package io.runon.trading.technical.analysis.indicators.market.nhnl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.candle.IdCandles;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 신고가 신저가 
 * 결과 데이터
 * @author macle
 */
@Data
public class NhnlData implements TimeNumber {

    public NhnlData(){

    }

    long time;
    int length = 0;
    BigDecimal ratio = BigDecimal.ZERO;

    IdCandles[] highs = IdCandles.EMPTY_ARRAY;
    IdCandles[] lows = IdCandles.EMPTY_ARRAY;

    public int getHighLength(){
        return highs.length;
    }
    public int getLowLength(){
        return lows.length;
    }

    @Override
    public String toString(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = new JsonObject();
        object.addProperty("time", time);
        object.addProperty("ratio", ratio);
        object.addProperty("length", length);
        object.addProperty("high_length", highs.length);
        object.addProperty("low_length", lows.length);
        return gson.toJson(object);
    }

    @Override
    public BigDecimal getNumber() {
        return ratio;
    }
}
