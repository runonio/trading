package io.runon.trading.data.daily;

import com.seomse.commons.config.Config;
import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.trading.data.DailyData;

import java.util.List;

/**
 * @author macle
 */
public class DailyDataJdbc {
    public static RatingScoreDaily [] getRatingScores(String dataKey, int beginYmd, int endYmd){


        List<DailyData> list = JdbcObjects.getObjList(DailyData.class ,"data_key='" + dataKey +"' and ymd >= " + beginYmd +" and ymd <=" + endYmd +" order by ymd asc");

        RatingScoreDaily [] dailies = new RatingScoreDaily[list.size()];

//        int idx = 0;
//
//        for (int i = dailies.length-1; i  > -1 ; i--) {
//            dailies[idx++] = RatingScoreDaily.make(list.get(i));
//        }

        for (int i = 0; i <dailies.length ; i++) {
            dailies[i] = RatingScoreDaily.make(list.get(i));
        }

        return dailies;
    }


    public static void main(String[] args) {
        Config.getConfig("");

        RatingScoreDaily [] dailies = getRatingScores("fear_and_greed",20230101,20230130);

        for(RatingScoreDaily daily : dailies){
            System.out.println(daily);
        }

    }
}
