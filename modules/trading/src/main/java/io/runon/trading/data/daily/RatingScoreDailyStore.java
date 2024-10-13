package io.runon.trading.data.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public class RatingScoreDailyStore {

    private final Map<String, RatingScoreDaily[] > dataMap = new HashMap<>();

    public void clear(){
        dataMap.clear();
    }

    public void putData(String [] dataKeys, int beginYmd, int endYmd){

        for(String dataKey: dataKeys){
            RatingScoreDaily [] dailies = DailyDataJdbc.getRatingScores(dataKey, beginYmd, endYmd);
            dataMap.put(dataKey, dailies);
        }
    }

    public String [] getKeys(){
        return dataMap.keySet().toArray(new String[0]);
    }

    public RatingScoreDaily[] getDailies(String dataKey){
        return dataMap.get(dataKey);
    }
}