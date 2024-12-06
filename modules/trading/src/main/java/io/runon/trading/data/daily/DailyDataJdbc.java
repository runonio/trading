package io.runon.trading.data.daily;

import io.runon.commons.config.Config;
import io.runon.jdbc.objects.JdbcObjects;
import io.runon.trading.data.DailyData;

import java.util.List;

/**
 * @author macle
 */
public class DailyDataJdbc {
    public static RatingScoreDaily [] getRatingScores(String dataKey, int beginYmd, int endYmd){

        List<DailyData> list = getDataList(dataKey, beginYmd, endYmd);
        RatingScoreDaily [] dailies = new RatingScoreDaily[list.size()];
        for (int i = 0; i <dailies.length ; i++) {
            dailies[i] = RatingScoreDaily.make(list.get(i));
        }
        return dailies;
    }

    public static List<DailyData> getDataList(String dataKey, int beginYmd, int endYmd){
        return  JdbcObjects.getObjList(DailyData.class ,"data_key='" + dataKey +"' and ymd >= " + beginYmd +" and ymd <=" + endYmd +" order by ymd asc");
    }


    public static void main(String[] args) {
        Config.getConfig("");

        RatingScoreDaily [] dailies = getRatingScores("fear_and_greed",20230101,20230130);

        for(RatingScoreDaily daily : dailies){
            System.out.println(daily);
        }

    }
}
