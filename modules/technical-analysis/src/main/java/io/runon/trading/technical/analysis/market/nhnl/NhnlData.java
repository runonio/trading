package io.runon.trading.technical.analysis.market.nhnl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 신고가 신저가 
 * 결과 데이터
 * @author macle
 */
@Data
public class NhnlData {

    public NhnlData(){

    }

    long time;
    int length = 0;
    BigDecimal index = BigDecimal.ZERO;

    SymbolCandle[] highs = SymbolCandle.EMPTY_ARRAY;
    SymbolCandle[] lows = SymbolCandle.EMPTY_ARRAY;

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
        object.addProperty("index", index);
        object.addProperty("length", length);
        object.addProperty("high_length", highs.length);
        object.addProperty("low_length", lows.length);
        return gson.toJson(object);
    }

}
