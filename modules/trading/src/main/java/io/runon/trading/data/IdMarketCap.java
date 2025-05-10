package io.runon.trading.data;

import io.runon.commons.utils.GsonUtils;
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
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    public static IdMarketCap make(String jsonText){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonText, IdMarketCap.class);
    }


    public static IdMarketCap [] makeArray(String jsonText){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.fromJson( jsonText , IdMarketCap[].class );
    }

    public static String toJsonText(IdMarketCap [] array){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(array);
    }
}

