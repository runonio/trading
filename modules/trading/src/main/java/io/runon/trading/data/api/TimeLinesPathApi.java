package io.runon.trading.data.api;

import io.runon.commons.http.HttpApiResponse;
import io.runon.commons.http.HttpApis;
import io.runon.commons.utils.time.Times;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.TimeFiles;
import io.runon.trading.data.file.TimeLine;
import io.runon.trading.data.file.TimeName;
import io.runon.trading.data.json.JsonOrgUtils;
import io.runon.trading.exception.TradingApiException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZoneId;

/**
 * @author macle
 */
@Slf4j
public class TimeLinesPathApi {


    public static String [] getLines(String dirPath, TimeLine timeLine, long beginTime, int count){
        //zonid 구하는 것 추가하기
        
        return getLines(dirPath, timeLine, beginTime, count, null);
    }

    public static String [] getLines(String dirPath, TimeLine timeLine, long beginTime, int count, ZoneId zoneId){


        String dataPath = TradingConfig.getTradingDataPath();

        if(dirPath.startsWith(dataPath)){
            dirPath = dirPath.substring(dataPath.length());
        }

        TimeName.Type timeNameType = TimeFiles.getPathTimeNameType(dirPath);

        JSONObject param = new JSONObject();
        param.put("dir_path", dirPath);
        param.put("time_name_type", timeNameType .toString());
        param.put("time_line_type", timeLine.toString());
        if(zoneId !=null)
            param.put("zone_id", zoneId.toString());
        param.put("begin_time", beginTime);
        param.put("count", count);

        HttpApiResponse response = HttpApis.postJson(TradingConfig.RUNON_API_ADDRESS + "/api/time/data/lines", param.toString());
        if( response.getResponseCode() != 200){
            throw new TradingApiException("response code:" + response.getResponseCode() +", " + response.getMessage());
        }

        JSONObject object = new JSONObject(response.getMessage());
        String code = object.getString("code");
        if(!code.equals("1")){
            throw new TradingApiException("data code: " + code + ", message: " + object.getString("message"));
        }

        JSONArray lineArray = object.getJSONArray("lines");
        return JsonOrgUtils.getStrings(lineArray);
    }

    public static void main(String[] args) {
        TimeLine timeLine = TimeLine.CSV;
        String dirPath = "bonds/futures/candle/USA_10_year/15m";
//        long beginTime = 623635200000L;
        long beginTime = System.currentTimeMillis() - Times.DAY_1;
        int count = 500;

        String [] lines = getLines(dirPath, timeLine, beginTime, count, TradingTimes.USA_ZONE_ID);
        for(String line : lines){
            System.out.println(line);
        }

        System.out.println(TradingConfig.getTradingDataPath());

        System.out.println(lines.length);

    }
}
