package io.runon.trading.data.management;

import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.FileLineOut;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.file.TimeLines;
import io.runon.trading.data.file.TimeName;

import java.time.ZoneId;

/**
 * @author macle
 */
public class DailyLinesOut {

    protected String initYmd = "19900101";
    protected int nextDay = 30;

    protected DailyLineGet dailyLineGet;

    protected boolean isLastLineCheck = true;

    protected ZoneId zoneId = TradingConfig.DEFAULT_TIME_ZONE_ID;

    protected PathTimeLine pathTimeLine = PathTimeLine.JSON;

    protected long sleepTime = 1500L;

    public DailyLinesOut(){
    }

    public void setInitYmd(String initYmd) {
        this.initYmd = initYmd;
    }

    public void setNextDay(int nextDay) {
        this.nextDay = nextDay;
    }

    public void setLastLineCheck(boolean lastLineCheck) {
        isLastLineCheck = lastLineCheck;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setPathTimeLine(PathTimeLine pathTimeLine) {
        this.pathTimeLine = pathTimeLine;
    }

    public void out(DailyLineGet dailyLineGet, String filesDirPath){
        out(dailyLineGet, filesDirPath, YmdUtil.now(zoneId));
    }

    public void out(DailyLineGet dailyLineGet, String filesDirPath, String lastEndYmd){
        String nextYmd ;

        long lastTime = pathTimeLine.getLastTime(filesDirPath);

        if(nextDay < 1){
            throw new IllegalArgumentException("next day < 1");
        }

        int lastEndYmdNum = Integer.parseInt(lastEndYmd);

        if(lastTime <= 0){
            //초기 정보가 없는 경우
            nextYmd = initYmd;

        }else{
            nextYmd = YmdUtil.getYmd(lastTime, TradingTimes.KOR_ZONE_ID);
            //초기 정보가 있는 경우
            if(!isLastLineCheck){
                // 마지막일자를 체크하지 않으면 마지막 저장 날짜의 다음날짜를 호출한다.
                nextYmd = YmdUtil.getYmd(nextYmd, 1);
            }
        }

        TimeName.Type timeNameType = TimeName.getCandleType(Times.DAY_1);

        boolean isFirst = true;

        for(;;){
            if (YmdUtil.compare(nextYmd, lastEndYmd) > 0) {
                break;
            }

            String endYmd = YmdUtil.getYmd(nextYmd, nextDay);

            int endYmdNum =  Integer.parseInt(endYmd);
            if(endYmdNum > lastEndYmdNum){
                endYmd = lastEndYmd;
            }

            String [] lines = dailyLineGet.getLines(nextYmd, endYmd);
            sleep();

            if(isLastLineCheck && isFirst) {

                FileLineOut.outBackPartChange(pathTimeLine, lines, filesDirPath, timeNameType, TradingTimes.KOR_ZONE_ID);
                isFirst = false;
            }else{
                FileLineOut.outNewLines(pathTimeLine, lines, filesDirPath, timeNameType, TradingTimes.KOR_ZONE_ID);
            }

            if(endYmdNum >= lastEndYmdNum){
                break;
            }

            if(lines.length == 0){
                nextYmd = YmdUtil.getYmd(endYmd, 1);
            }else{
                nextYmd = YmdUtil.getYmd(TimeLines.getMaxYmd(pathTimeLine, lines, zoneId),1);
            }
        }


    }


    public void sleep(){
        try{
            Thread.sleep(sleepTime);
        }catch (Exception ignore){}
    }

}
