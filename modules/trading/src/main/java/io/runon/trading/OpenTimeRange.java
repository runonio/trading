package io.runon.trading;

import lombok.Data;
/**
 * @author macle
 */
@Data
public class OpenTimeRange {

    long beginOpenTime;
    long endOpenTime;


    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
