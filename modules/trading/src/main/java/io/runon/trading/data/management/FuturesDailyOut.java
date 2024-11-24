package io.runon.trading.data.management;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.time.Times;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.trading.data.Futures;
import io.runon.trading.data.FuturesData;
import io.runon.trading.data.FuturesUtils;
import io.runon.trading.data.TradingDataManager;
import io.runon.trading.data.file.FileLineOut;
import io.runon.trading.data.file.PathTimeLine;
import io.runon.trading.data.file.TimeLines;
import io.runon.trading.data.file.TimeName;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;

/**
 * @author macle
 */
@Slf4j
public class FuturesDailyOut {


    protected PathTimeLine pathTimeLine;
    protected FuturesPathLastTime pathLastTime;

    protected FuturesDailyOutParam param;

    public FuturesDailyOut(FuturesDailyOutParam dailyOutParam){
        this.param = dailyOutParam;
        this.pathLastTime = dailyOutParam.getFuturesPathLastTime();
        this.pathTimeLine = dailyOutParam.getPathTimeLine();
        this.isListed = dailyOutParam.isListed();
    }

    //현재 상장종목만 api를 제공하는 증권사인 ls증권 데이터를 주로이용한다. 관련옵션은 과거 정보를 주지않는 증권사를 쓸경우 true, 상폐된 종목의 정보를 얻을 수 있는 증권사의경우 false로 설정한다.
    protected boolean isListed= true;

    public void setListed(boolean listed) {
        isListed = listed;
    }

    protected boolean isLastLineCheck = true;

    protected String serviceName;

    public void setPathTimeLine(PathTimeLine pathTimeLine) {
        this.pathTimeLine = pathTimeLine;
    }

    public void setPathLastTime(FuturesPathLastTime pathLastTime) {
        this.pathLastTime = pathLastTime;
    }


    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setLastLineCheck(boolean lastLineCheck) {
        isLastLineCheck = lastLineCheck;
    }


    public void out(String ymd){
        FuturesData futuresData = TradingDataManager.getInstance().getFuturesData();

        Futures [] array ;
        if(isListed){
            array = futuresData.getListedFutures(param.getExchange(), ymd);
        }else{
            array = futuresData.getFutures(param.getExchange(), ymd);
        }

        for(Futures futures :array){
            out(futures);
        }
    }

    public void out(Futures futures){

        this.pathLastTime = param.getFuturesPathLastTime();
        this.pathTimeLine = param.getPathTimeLine();

        ZoneId zoneId = FuturesUtils.getZoneId(futures);

        String nowYmd = YmdUtil.now(zoneId);
        int nowYmdNum = Integer.parseInt(nowYmd);

        //초기 데이터는 상장 년원일
        String nextYmd ;

        String filesDirPath = pathLastTime.getFilesDirPath(futures, "1d");

        long lastTime = pathTimeLine.getLastTime(filesDirPath);

        int lastTradeCheckYmd = Integer.parseInt(YmdUtil.getYmd(nowYmd, 7));

        String lastTimeFilePath = pathLastTime.getLastTimeFilePath(futures,"1d");
        try{
            String line = FileUtil.getLastTextLine(lastTimeFilePath);
            if(!line.isEmpty()){
                lastTime = Long.parseLong(line);
            }
        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }

        if(lastTime > -1){

            nextYmd = YmdUtil.getYmd(lastTime, zoneId);
            if(!isLastLineCheck){
                // 마지막일자를 체크하지 않으면 마지막 저장 날짜의 다음날짜를 호출한다.
                nextYmd = YmdUtil.getYmd(nextYmd, 1);
            }

            if(isLastLineCheck && futures.getLastTradingYmd() != null){
                //최종 거래일이 일주일전보다 작으면 상폐되었을 확율이 높음.
                if(futures.getLastTradingYmd() < lastTradeCheckYmd){
                    return;
                }
            }else if(isLastLineCheck && futures.getSettlementYmd() != null){
                if(futures.getSettlementYmd() < lastTradeCheckYmd){
                    return;
                }
            }

        }else{
            //상장년월일 이용
            //상장년원일이 없을경우 데이터를 모으지 않음
            if(futures.getListedYmd() == null){
                log.error("listed ymd null: " + futures);
                return ;
            }
            nextYmd = Integer.toString(futures.getListedYmd());
        }

        TimeName.Type timeNameType = TimeName.getDefaultType(Times.DAY_1);

        boolean isFirst = true;

        if(serviceName == null) {
            log.debug("start futures: " + futures);
        }else{
            log.debug(serviceName + " start futures: " + futures);
        }

        int maxYmdInt = nowYmdNum;

        if(futures.getLastTradingYmd() != null){
            maxYmdInt = futures.getLastTradingYmd();
        }else if(futures.getSettlementYmd() != null){
            maxYmdInt = futures.getSettlementYmd();
        }
        maxYmdInt = Math.min(maxYmdInt, nowYmdNum);

        String maxYmd = Integer.toString(maxYmdInt);

        for(;;){

            if (YmdUtil.compare(nextYmd, maxYmd) > 0) {
                break;
            }

            String endYmd = YmdUtil.getYmd(nextYmd, param.getNextDay());

            int endYmdNum =  Integer.parseInt(endYmd);
            if(endYmdNum > maxYmdInt){
                endYmd = Integer.toString(maxYmdInt);
            }

            String [] lines = param.getLines(futures, nextYmd, endYmd);
            param.sleep();

            if(lines.length > 0) {
                if (isLastLineCheck && isFirst) {

                    FileLineOut.outBackPartChange(pathTimeLine, lines, filesDirPath, timeNameType);
                    isFirst = false;
                } else {
                    FileLineOut.outNewLines(pathTimeLine, lines, filesDirPath, timeNameType);
                }
                long maxTime = TimeLines.getMaxTime(PathTimeLine.CSV, lines);
                FileUtil.fileOutput(Long.toString(maxTime), lastTimeFilePath, false);
            }else{
                long maxTime = YmdUtil.getTime(endYmd, zoneId);
                FileUtil.fileOutput(Long.toString(maxTime), lastTimeFilePath, false);
            }

            if(endYmdNum >= maxYmdInt){
                break;
            }

            if(lines.length == 0){
                nextYmd = YmdUtil.getYmd(endYmd, 1);
            }else{
                nextYmd = YmdUtil.getYmd(TimeLines.getMaxYmd(param.getPathTimeLine(), lines, zoneId),1);
            }
        }
    }
}
