package io.runon.trading.data;

import io.runon.trading.Time;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class RatingScore implements Time {

    long time;

    BigDecimal score;

    String rating;

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    public static RatingScore make(String jsonText){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonText, RatingScore.class);
    }

}
