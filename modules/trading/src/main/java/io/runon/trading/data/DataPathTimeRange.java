package io.runon.trading.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingConfig;
import lombok.Data;
/**
 * 데이터 시간 범위
 * @author macle
 */
@Data
public class DataPathTimeRange {
    private String dataPath;
    private long beginTime;
    private long endTime;

    public DataPathTimeRange(){

    }

    public DataPathTimeRange(String dataPath, long beginTime, long endTime){
        this.dataPath = dataPath;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    @Override
    public String toString(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data_path" , dataPath);
        jsonObject.addProperty("begin_time" , Times.ymdhm(beginTime, TradingConfig.VIEW_TIME_ZONE_ID));
        jsonObject.addProperty("end_time" , Times.ymdhm(endTime, TradingConfig.VIEW_TIME_ZONE_ID));
        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }

}
