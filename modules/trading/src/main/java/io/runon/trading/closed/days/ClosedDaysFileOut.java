package io.runon.trading.closed.days;

import io.runon.commons.callback.StrCallback;
import io.runon.commons.utils.ExceptionUtils;
import io.runon.commons.utils.FileUtils;
import io.runon.commons.utils.time.YmdUtils;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.YmdFiles;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * 과거일 사용일 정보를 저장한다.
 * 벡테스팅용 휴장일이기때문에 오늘날까까지 저장한다
 * @author macle
 */
@Slf4j
public class ClosedDaysFileOut{


    private final ClosedDaysCallback closedDaysCallback;
    private String lastCheckYmd = null;

    private final CountryCode countryCode;

    public ClosedDaysFileOut(ClosedDaysCallback closedDaysGet, CountryCode countryCode){
        this.closedDaysCallback = closedDaysGet;
        this.countryCode = countryCode;
    }

    private boolean isChange = false;
    public synchronized boolean out() {

        isChange = false;
        try{
            String nowYmd = YmdUtils.now(TradingTimes.KOR_ZONE_ID);

            if(lastCheckYmd != null && lastCheckYmd.equals(nowYmd)){
                return isChange;
            }

            lastCheckYmd = nowYmd;

            String fileSeparator = FileSystems.getDefault().getSeparator();
            String filePath = ClosedDays.getCloseDaysFilePath(countryCode);
            String beginYmd = null;

            if(FileUtils.isFile(filePath)){

                beginYmd = FileUtils.getLastTextLine(filePath);
                beginYmd = YmdUtils.getYmd(beginYmd,1);
            }

            if(beginYmd == null){
                beginYmd = "19900101";
            }

            if(YmdUtils.compare(beginYmd,nowYmd) > 0){
                return isChange;
            }


            final List<String> ymdList = new ArrayList<>();

            StrCallback callback = ymd -> {
                ymdList.add(ymd);

                if(ymdList.size() > 100){
                    isChange = true;
                    YmdFiles.outAppend(filePath, ymdList);
                    ymdList.clear();
                }
            };


            closedDaysCallback.callbackClosedDays(beginYmd, nowYmd, callback);

            if(!ymdList.isEmpty()){
                isChange = true;
                YmdFiles.outAppend(filePath, ymdList);
                ymdList.clear();
            }

        }catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return isChange;
    }
}
