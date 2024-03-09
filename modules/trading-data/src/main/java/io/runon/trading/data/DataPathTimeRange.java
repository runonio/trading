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
    private long startTime;
    private long endTime;

    public DataPathTimeRange(){

    }

    public DataPathTimeRange(String dataPath, long startTime, long endTime){
        this.dataPath = dataPath;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data_path" , dataPath);
        jsonObject.addProperty("start_time" , Times.ymdhm(startTime, TradingConfig.viewTimeZoneId));
        jsonObject.addProperty("end_time" , Times.ymdhm(endTime, TradingConfig.viewTimeZoneId));
        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }

}
