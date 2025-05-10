package io.runon.trading.data;

import io.runon.commons.utils.GsonUtils;
import io.runon.trading.Time;
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
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }


    public static RatingScore make(String jsonText){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonText, RatingScore.class);
    }

}
