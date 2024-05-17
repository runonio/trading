package io.runon.trading.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.trading.TradingConfig;
import lombok.Data;

import java.time.ZoneId;

/**
 * 데이터 시간 범위
 * @author macle
 */
@Data
public class DataPathTimeRange {
    private String dataPath;
    private long beginTime;
    private long endTime;

    private ZoneId zoneId = null;

    public DataPathTimeRange(){

    }

    public DataPathTimeRange(String dataPath, long beginTime, long endTime){
        this.dataPath = dataPath;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data_path" , dataPath);
        if(zoneId != null){
            jsonObject.addProperty("begin_time" , Times.ymdhm(beginTime, zoneId));
            jsonObject.addProperty("end_time" , Times.ymdhm(endTime, zoneId));
            jsonObject.addProperty("time_zone_id", zoneId.toString());
        }else{
            jsonObject.addProperty("begin_time" , Times.ymdhm(beginTime, TradingConfig.DEFAULT_TIME_ZONE_ID));
            jsonObject.addProperty("end_time" , Times.ymdhm(endTime, TradingConfig.DEFAULT_TIME_ZONE_ID));
        }


        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }


    public int getBeginYmd(){
        return Integer.parseInt(getBeginYmdText());
    }

    public String getBeginYmdText(){
        ZoneId zoneId = this.zoneId;
        if(zoneId == null){
            zoneId =  TradingConfig.DEFAULT_TIME_ZONE_ID;
        }

       return YmdUtil.getYmd(beginTime, zoneId);
    }


    public int getEndYmd(){
        return Integer.parseInt(getEndYmdText());
    }

    public String getEndYmdText(){
        ZoneId zoneId = this.zoneId;
        if(zoneId == null){
            zoneId =  TradingConfig.DEFAULT_TIME_ZONE_ID;
        }

        return YmdUtil.getYmd(endTime, zoneId);
    }

}
