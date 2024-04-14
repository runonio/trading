package io.runon.trading.closed.days;

import com.seomse.commons.callback.StrCallback;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.time.YmdUtil;
import io.runon.trading.TradingConfig;
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


    private final ClosedDaysCallback closedDaysGet;
    private String lastCheckYmd = null;

    public ClosedDaysFileOut(ClosedDaysCallback closedDaysGet){
        this.closedDaysGet = closedDaysGet;
    }

    public void out() {

        try{
            String nowYmd = YmdUtil.now(TradingTimes.KOR_ZONE_ID);

            if(lastCheckYmd != null && lastCheckYmd.equals(nowYmd)){
                return;

            }

            lastCheckYmd = nowYmd;

            String fileSeparator = FileSystems.getDefault().getSeparator();
            String filePath = TradingConfig.getTradingDataPath() + fileSeparator + "market/closed_days/KOR_closed_days.txt";
            String beginYmd = null;

            if(FileUtil.isFile(filePath)){
                beginYmd = FileUtil.getLastTextLine(filePath);
                beginYmd = YmdUtil.getYmd(beginYmd,1);
            }

            if(beginYmd == null){
                beginYmd = "19900101";
            }

            if(YmdUtil.compare(beginYmd,nowYmd) > 0){
                return;
            }


            final List<String> ymdList = new ArrayList<>();

            StrCallback callback = ymd -> {
                ymdList.add(ymd);

                if(ymdList.size() > 100){
                    YmdFiles.outAppend(filePath, ymdList);
                    ymdList.clear();
                }
            };

            closedDaysGet.callbackClosedDays(beginYmd, nowYmd, callback);

            if(!ymdList.isEmpty()){
                YmdFiles.outAppend(filePath, ymdList);
                ymdList.clear();
            }

        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
    }
}
