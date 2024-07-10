package io.runon.trading.data.api;

import com.seomse.commons.http.HttpApiResponse;
import com.seomse.commons.http.HttpApis;
import io.runon.trading.TradingConfig;
import io.runon.trading.data.file.TimeLine;

/**
 * @author macle
 */
public class TimeLinesPathApi {

    public static String [] getLines(String dirPath, TimeLine timeLine, long beginTime, int count){


        HttpApiResponse response = HttpApis.postJson(TradingConfig.RUNON_API_ADDRESS + "/api/time/data/lines","");

        return null;
    }

    public static void main(String[] args) {
        TimeLine timeLine = TimeLine.CSV;
        String dirPath = "D:\\runon\\data\\bonds\\futures\\candle\\USA_10_year\\15m";

//        getLines("", TimeLine.JSON, )
    }
}
