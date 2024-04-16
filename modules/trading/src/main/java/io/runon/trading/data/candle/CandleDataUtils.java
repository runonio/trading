package io.runon.trading.data.candle;

import com.seomse.commons.utils.FileUtil;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.CsvTimeLine;
import io.runon.trading.data.file.TimeFileOverride;
import io.runon.trading.data.file.TimeName;

import java.io.File;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
/**
 * @author macle
 */
public class CandleDataUtils {


    public static String [] getCandlePaths(String dirPath){
        List<File> dirList = FileUtil.getDirList(dirPath);

        List<String> pathList = new ArrayList<>();

        for(File dir : dirList){
            boolean isParentsCandle = false;
            File parents = dir.getParentFile();
            if(parents == null){
                continue;
            }
            if(parents.getName().equals("candle")){
                isParentsCandle = true;
            }else{

                parents = parents.getParentFile();
                if(parents == null){
                    continue;
                }
                if(parents.getName().equals("candle")){
                    isParentsCandle = true;
                }
            }

            if(isParentsCandle && TradingTimes.isInterval(dir.getName())){
                pathList.add(dir.getAbsolutePath());
            }

        }

        String [] paths = pathList.toArray(new String[0]);
        pathList.clear();

        return paths;
    }


    public static void changeTimeZone(String dirPath, ZoneId zoneId){
        String [] candlePaths = getCandlePaths(dirPath);
        for(String candlePath : candlePaths){
            long intervalTime = TradingTimes.getIntervalTime(new File(candlePath).getName());
            TimeFileOverride timeFileOverride =  new TimeFileOverride(candlePath, new CsvTimeLine(), TimeName.getCandleType(intervalTime));
            timeFileOverride.setZoneId(zoneId);
            timeFileOverride.run();
        }
    }

}