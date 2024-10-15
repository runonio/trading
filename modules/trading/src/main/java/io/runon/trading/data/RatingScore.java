package io.runon.trading.data;

import io.runon.trading.Time;
import io.runon.trading.TradingGson;
import io.runon.trading.data.json.JsonOutLine;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author macle
 */
@Data
public class RatingScore implements Time {

    public static final Comparator<RatingScore> SORT = Comparator.comparingLong(o -> o.time);

    protected long time;

    protected BigDecimal score;

    protected String rating;

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }


    public static RatingScore make(String jsonText){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonText, RatingScore.class);
    }

}
