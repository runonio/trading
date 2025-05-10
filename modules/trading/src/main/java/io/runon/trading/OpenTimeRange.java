package io.runon.trading;

import io.runon.commons.utils.GsonUtils;
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
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
