package io.runon.trading;

import io.runon.commons.utils.GsonUtils;
import lombok.Data;
/**
 * @author macle
 */
@Data
public class BeginEndTimeData implements BeginEndTime{

    protected long beginTime;
    protected long endTime;

    public BeginEndTimeData(){

    }

    public BeginEndTimeData(long beginTime, long endTime){
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
