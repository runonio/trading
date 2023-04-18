package io.runon.trading.data.time;

import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.TimeName;
import io.runon.trading.data.file.TimeNameImpl;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 문자열을 활용한 lock 관리
 * @author macle
 */
public class LineOutManager {
    private static class Singleton {
        private static final LineOutManager instance = new LineOutManager();
    }

    public static LineOutManager getInstance(){
        return Singleton.instance;
    }

    private final Object lock = new Object();
    
    private final Map<String, TimeLineOut> lockMap = new HashMap<>();

    public static final CsvTimeLine csvTimeLine = new CsvTimeLine();

    public TimeLineOut get(String dirPath, TimeLine timeLine, TimeName timeName){
        synchronized (lock){
            TimeLineOut timeLineOut =  lockMap.get(dirPath);
            if(timeLineOut == null){
                timeLineOut = new TimeLineOut(dirPath, timeLine, timeName);
                lockMap.put(dirPath, timeLineOut);
            }
            return timeLineOut;
        }

    }

    public TimeLineOut get(String dirPath){
        synchronized (lock){
            return lockMap.get(dirPath);
        }
    }

    public TimeLineOut getCandleLineOut(String dirPath){
        return getCandleLineOut(dirPath, null);
    }

    public TimeLineOut getCandleLineOut(String dirPath, ZoneId zoneId){
        synchronized (lock){
            TimeLineOut timeLineOut = lockMap.get(dirPath);
            if(timeLineOut == null){
                String intervalGet = dirPath.replace("\\","/");
                int lastIndex = intervalGet.lastIndexOf("/");
                String interval = intervalGet.substring(lastIndex+1);
                long intervalTime = TradingTimes.getIntervalTime(interval);
                TimeNameImpl timeName = new TimeNameImpl(TimeName.getCandleType(intervalTime));
                if(zoneId != null){
                    timeName.setZoneId(zoneId);
                }
                timeLineOut = new TimeLineOut(dirPath, csvTimeLine, timeName);
                lockMap.put(dirPath, timeLineOut);
            }
            return timeLineOut;
        }

    }


}
