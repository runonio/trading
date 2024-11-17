package io.runon.trading.data.api;

import com.seomse.commons.utils.FileUtil;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.FileLineOut;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.file.TimeFiles;
import io.runon.trading.data.file.TimeLines;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author macle
 */
@Slf4j
public class ApiDataSync {
    public static void candleSyncAll(){
//        String [] dirs = TimeFiles.getApiCandleDataIntervalDirs();
        String [] dirs = FileUtil.getFileContents("config/candle_sync_path.txt", StandardCharsets.UTF_8).split("\n");

        for(String dir: dirs){
            log.debug("candle sync dir: " + dir);
            dataSync(dir, PathTimeLine.CSV);
        }
    }

    public static void dataSync(String dirRelativePath, String timeLineType){
        PathTimeLine pathTimeLine = PathTimeLine.getPathTimeLine(timeLineType);
        dataSync(dirRelativePath, pathTimeLine);
    }

    public static void dataSync(String dirRelativePath, PathTimeLine pathTimeLine){

        String dataPath = TradingConfig.getTradingDataPath();
        dataPath = dataPath.replace("\\","/");
        dirRelativePath = dirRelativePath.replace("\\" ,"/");

        if(dirRelativePath.startsWith(dataPath+"/")){
            dirRelativePath = dirRelativePath.substring(dataPath.length()+1);
        }


        String dataAbsolutePath = dataPath + "/" + dirRelativePath;
        long lastTime = pathTimeLine.getLastTime(dataAbsolutePath);
        //마지막 라인 시간을 그대로 전송하면 마지막 라인부터 다시 받는다
        //마지막 라인은 실시간 데이터일 수 있으므로 변경가능 해야한다.


        int count = 500;

        long beginTime = lastTime;

        for(;;){
            String [] lines =  TimeLinesPathApi.getLines(dirRelativePath, pathTimeLine, beginTime, count);
            if(lines.length == 0){
                break;
            }

            FileLineOut.outBackPartChange(pathTimeLine, lines, dataAbsolutePath,  TimeFiles.getPathTimeNameType(dirRelativePath));

            try{
                Thread.sleep(TradingConfig.RUNON_API_SLEEP_TIME);
            }catch (Exception ignore){}

            beginTime = TimeLines.getMaxTime(pathTimeLine, lines)+1;
            if(lines.length < count){
                break;
            }
        }

    }

    public static void main(String[] args) {
        candleSyncAll();
    }


}
