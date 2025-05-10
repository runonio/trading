package io.runon.trading.data.daily;

import com.google.gson.JsonObject;
import io.runon.commons.utils.GsonUtils;
import io.runon.trading.Time;
import io.runon.trading.data.DailyData;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 일별 숫자형 데이터 정보
 * @author macle
 */
@Data
public class RatingScoreDaily implements Time {


    long t;

    BigDecimal score;
    String rating;

    int ymd;

    public static RatingScoreDaily make(DailyData dailyData){
        JsonObject object = GsonUtils.LOWER_CASE_WITH_UNDERSCORES.fromJson(dailyData.getDataValue(), JsonObject.class);
        RatingScoreDaily ratingScoreDaily = new RatingScoreDaily();
        ratingScoreDaily.t = object.get("time").getAsLong();
        ratingScoreDaily.score = object.get("score").getAsBigDecimal();
        ratingScoreDaily.rating = object.get("rating").getAsString();
        ratingScoreDaily.setYmd(dailyData.getYmd());
        return ratingScoreDaily;
    }

    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    @Override
    public long getTime() {
        return t;
    }

}
