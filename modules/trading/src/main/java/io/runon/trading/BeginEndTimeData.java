package io.runon.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
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
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}
