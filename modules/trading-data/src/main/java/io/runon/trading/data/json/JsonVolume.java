package io.runon.trading.data.json;

import io.runon.trading.technical.analysis.candle.CandleTime;
import io.runon.trading.technical.analysis.volume.TimeVolumes;
import io.runon.trading.technical.analysis.volume.VolumeData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;

/**
 * json 파일을 활용한 거래량 정보
 * @author macle
 */
public class JsonVolume {

    public static TimeVolumes makeTimeVolumes(String jsonText){
        JSONObject obj = new JSONObject(jsonText);

        TimeVolumes timeVolumes = new TimeVolumes();

        timeVolumes.setTime(obj.getLong("t"));
        timeVolumes.setPrice(obj.getBigDecimal("p"));

        obj.remove("t");
        obj.remove("p");

        if(!obj.isNull("pf")){
            timeVolumes.setPriceFutures( obj.getBigDecimal("pf"));
            obj.remove("pf");
        }

        if(!obj.isNull("avg_5s")){
            timeVolumes.setAvg5s(obj.getBigDecimal("avg_5s"));
            obj.remove("avg_5s");
        }

        if(!obj.isNull("avg_1m")){
            timeVolumes.setAvg1m(obj.getBigDecimal("avg_1m"));
            obj.remove("avg_1m");
        }

        Set<String> keys = obj.keySet();
        for(String interval : keys){
            JSONArray array = obj.getJSONArray(interval);
            VolumeData volumeData = new VolumeData();
            volumeData.setVolume(array.getBigDecimal(0));
            volumeData.setTradingPrice(array.getBigDecimal(1));
            volumeData.setVolumePower(array.getBigDecimal(2));
            timeVolumes.put(CandleTime.getIntervalTime(interval), volumeData);
        }

        obj.clear();

        return timeVolumes;
    }
}
