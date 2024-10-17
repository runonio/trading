package io.runon.trading.data;

import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;
/**
 * @author macle
 */
@Data
public class IdMarketCap {
    String id;

    BigDecimal close;

    BigDecimal marketCap;


    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    public static IdMarketCap make(String jsonText){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonText, IdMarketCap.class);
    }


    public static IdMarketCap [] makeArray(String jsonText){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson( jsonText , IdMarketCap[].class );
    }

    public static String toJsonText(IdMarketCap [] array){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(array);
    }
}

