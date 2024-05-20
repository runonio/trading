package io.runon.trading.technical.analysis.candle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.runon.trading.TradingGson;
import lombok.Data;
/**
 * 캔들과 전 캔들
 * @author macle
 */
@Data
public class CandlePreviousCandles {
    String id;
    CandlePreviousCandle [] array;

    @Override
    public String toString(){

        JsonObject object = new JsonObject();
        object.addProperty("id", id);

        if(array != null){
            JsonArray jsonArray = new JsonArray();
            for(CandlePreviousCandle c : array){
                jsonArray.add(c.jsonArray());
            }
            object.add("outputs", jsonArray);
        }

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(object);
    }

}
