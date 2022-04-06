package io.runon.trading.data.json;

import io.runon.trading.CandleTimes;
import io.runon.trading.technical.analysis.volume.TimeVolumes;
import io.runon.trading.technical.analysis.volume.VolumeData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

/**
 * json 파일을 활용한 거래량 정보
 * @author macle
 */
public class JsonVolume {


    public static TimeVolumes makeTimeVolumes(String jsonText){
        return makeTimeVolumes(jsonText, null);
    }

    public static TimeVolumes makeTimeVolumes(String jsonText, TimeVolumes last){
        JSONObject obj = new JSONObject(jsonText);

        TimeVolumes timeVolumes = new TimeVolumes();

        timeVolumes.setTime(obj.getLong("t"));
        timeVolumes.setPrice(obj.getBigDecimal("p"));


        obj.remove("t");
        obj.remove("p");

        if(obj.has("pf")){
            timeVolumes.setPriceFutures( obj.getBigDecimal("pf"));
            obj.remove("pf");
        }

        if(last != null){
            BigDecimal lastPrice = last.getPrice();
            BigDecimal price = timeVolumes.getPrice();
            timeVolumes.setChangeRate(price.subtract(lastPrice).divide(lastPrice,8, RoundingMode.HALF_UP).stripTrailingZeros());
        }
        if(obj.has("p_cr")){
            if(last == null) {
                timeVolumes.setChangeRate(obj.getBigDecimal("p_cr"));
            }
            obj.remove("p_cr");
        }

        if(last != null){
            BigDecimal lastPriceFutures = last.getPriceFutures();
            BigDecimal priceFutures = timeVolumes.getPriceFutures();
            timeVolumes.setChangeRateFutures(priceFutures.subtract(lastPriceFutures).divide(lastPriceFutures,8, RoundingMode.HALF_UP).stripTrailingZeros());
        }
        if(obj.has("pf_cr")){
            if(last == null) {
                timeVolumes.setChangeRateFutures(obj.getBigDecimal("pf_cr"));
            }
            obj.remove("pf_cr");
        }

        if(obj.has("avg_5s")){
            timeVolumes.setAvg5s(obj.getBigDecimal("avg_5s"));
            obj.remove("avg_5s");
        }

        if(obj.has("avg_1m")){
            timeVolumes.setAvg1m(obj.getBigDecimal("avg_1m"));
            obj.remove("avg_1m");
        }

        Set<String> keys = obj.keySet();
        for(String interval : keys){
            long time = -1;
            try{
                time = CandleTimes.getIntervalTime(interval);
            }catch(IllegalArgumentException ignore){}

            JSONArray array = obj.getJSONArray(interval);
            VolumeData volumeData = new VolumeData();
            volumeData.setVolume(array.getBigDecimal(0));
            volumeData.setTradingPrice(array.getBigDecimal(1));
            volumeData.setVolumePower(array.getBigDecimal(2));
            timeVolumes.put(time, volumeData);
        }

        obj.clear();

        return timeVolumes;
    }
}
