package io.runon.trading.data.api;

import io.runon.trading.TradingConfig;
import io.runon.trading.data.file.PathTimeLine;

/**
 * @author macle
 */
public class ApiDataSync {
    public static void candleSyncAll(){

    }


    public static void dataSync(String dirRelativePath, String timeLineType){
        PathTimeLine pathTimeLine = PathTimeLine.getPathTimeLine(timeLineType);
        dataSync(dirRelativePath, pathTimeLine);
    }

    public static void dataSync(String dirRelativePath, PathTimeLine pathTimeLine){
        String dataPath = TradingConfig.getTradingDataPath() + "/" + dirRelativePath;
        long lastTime = pathTimeLine.getLastTime(dataPath);
        //마지막 라인 시간을 그대로 전송하면 마지막 라인부터 다시 받는다
        //마지막 라인은 실시간 데이터일 수 있으므로 변경가능 해야한다.


    }


}
