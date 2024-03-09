package io.runon.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
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
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}
