package io.runon.trading.data.jdbc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.runon.trading.TradingGson;

/**
 * trading 에서 사용하는 db관련 유틸성
 * @author macle
 */
public class TradingJdbc {

    public static boolean equalsOutUpdatedAt(Object source, Object target){

        String sourceOutUpdatedAt = getOutUpdateAtJson(source);
        String targetSourceOutUpdatedAt = getOutUpdateAtJson(target);

        return sourceOutUpdatedAt.equals(targetSourceOutUpdatedAt);

    }


    public static String getOutUpdateAtJson(Object object){
        Gson gson = TradingGson.LOWER_CASE_WITH_UNDERSCORES;
        String jsonText = gson.toJson(object);

        JsonObject jsonObject = gson.fromJson(jsonText, JsonObject.class);
        jsonObject.remove("updated_at");

        return gson.toJson(jsonObject);
    }



}
