package io.runon.trading.technical.analysis.candle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Data;

/**
 * 종목 구분 기화와 캔들 구현체
 * @author macle
 */
@Data
public class IdCandleData implements IdCandles {
    private String id;
    private TradeCandle [] candles;

    public IdCandleData(){

    }

    public IdCandleData(String symbol, TradeCandle [] candles){
        this.id = symbol;
        this.candles = candles;
    }


    @Override
    public String toString(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = new JsonObject();
        object.addProperty("symbol", id);
        if(candles == null){
            object.addProperty("length", 0);
        }else{
            object.addProperty("length", candles.length);
        }

        return gson.toJson(object);
    }


}
