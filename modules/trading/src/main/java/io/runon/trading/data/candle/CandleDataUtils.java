package io.runon.trading.data.candle;

import io.runon.commons.utils.FileUtil;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.TimeFileOverride;
import io.runon.trading.data.file.TimeLine;
import io.runon.trading.data.file.TimeName;

import java.io.File;
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


    public static void changeTimeZone(String dirPath){
        String [] candlePaths = getCandlePaths(dirPath);
        for(String candlePath : candlePaths){
            long intervalTime = TradingTimes.getIntervalTime(new File(candlePath).getName());
            TimeFileOverride timeFileOverride =  new TimeFileOverride(candlePath, TimeLine.CSV, TimeName.getDefaultType(intervalTime));
            timeFileOverride.run();
        }
    }

}