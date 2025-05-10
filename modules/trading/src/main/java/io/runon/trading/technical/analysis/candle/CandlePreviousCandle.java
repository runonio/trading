package io.runon.trading.technical.analysis.candle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.runon.commons.utils.GsonUtils;
import lombok.Data;

/**
 * 캔들과 전 캔들
 * @author macle
 */
@Data
public class CandlePreviousCandle {

    TradeCandle candle;
    TradeCandle previousCandle;

    public CandlePreviousCandle(){

    }

    public CandlePreviousCandle(TradeCandle candle, TradeCandle previousCandle){
        this.candle = candle;
        this.previousCandle = previousCandle;
    }


    @Override
    public String toString(){

        JsonArray array = jsonArray();
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(array);
    }


    public JsonArray jsonArray(){
        JsonArray array = new JsonArray();
        array.add(GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.fromJson(candle.toString(), JsonObject.class));
        array.add(GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.fromJson(previousCandle.toString(), JsonObject.class));

        return array;
    }


}
