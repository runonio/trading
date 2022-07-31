package io.runon.trading.technical.analysis.indicator.volume;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.runon.trading.technical.analysis.SymbolCandle;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 거래량이 급등하는 종목의 수를 지수화 결과 데이터
 *
 * @author macle
 */
@Data
public class SoaringTradingVolumeData {

    SymbolCandle [] soaringArray = SymbolCandle.EMPTY_ARRAY;
    BigDecimal index = BigDecimal.ZERO;

    int length = 0;

    public int length(){
        return soaringArray.length;
    }
    @Override
    public String toString(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = new JsonObject();
        object.addProperty("index", index);
        object.addProperty("soaring_length", soaringArray.length);
        object.addProperty("length", length);
        return gson.toJson(object);
    }

}
