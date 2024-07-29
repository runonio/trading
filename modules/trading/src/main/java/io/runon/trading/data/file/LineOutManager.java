package io.runon.trading.data.file;

import io.runon.trading.TradingTimes;

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
    
    private final Map<String, TimeLineLock> lockMap = new HashMap<>();


    public TimeLineLock get(String dirPath, TimeLine timeLine, TimeName timeName){
        synchronized (lock){
            TimeLineLock timeLineOut =  lockMap.get(dirPath);
            if(timeLineOut == null){
                timeLineOut = new TimeLineLock(dirPath, timeLine, timeName);
                lockMap.put(dirPath, timeLineOut);
            }
            return timeLineOut;
        }

    }

    public TimeLineLock get(String dirPath){
        synchronized (lock){
            return lockMap.get(dirPath);
        }
    }

    public TimeLineLock getTimeLineLock(String dirPath){
        return getTimeLineLock(dirPath, null);
    }

    public TimeLineLock getTimeLineLock(String dirPath, ZoneId zoneId){
        synchronized (lock){
            TimeLineLock timeLineOut = lockMap.get(dirPath);
            if(timeLineOut == null){
                String intervalGet = dirPath.replace("\\","/");
                int lastIndex = intervalGet.lastIndexOf("/");
                String interval = intervalGet.substring(lastIndex+1);
                long intervalTime = TradingTimes.getIntervalTime(interval);
                TimeNameImpl timeName = new TimeNameImpl(TimeName.getDefaultType(intervalTime));
                if(zoneId != null){
                    timeName.setZoneId(zoneId);
                }
                timeLineOut = new TimeLineLock(dirPath, TimeLine.CSV, timeName);
                lockMap.put(dirPath, timeLineOut);
            }
            return timeLineOut;
        }

    }




}
