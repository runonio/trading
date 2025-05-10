package io.runon.trading.data.calendar;

import io.runon.commons.utils.GsonUtils;
import lombok.Data;

import java.math.BigDecimal;
/**
 * @author macle
 */
@Data
public class EconomicCalendarValue {
    //예측
    BigDecimal forecast;
    //실제
    BigDecimal actual;
    //이전
    BigDecimal previous;

    public static EconomicCalendarValue make(String jsonText){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonText, EconomicCalendarValue.class);
    }

    public String toString(){

        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }
}
