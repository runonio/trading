package io.runon.trading.closed.days;

import io.runon.commons.utils.time.YmdUtil;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingConfig;

import java.nio.file.FileSystems;

/**
 * 휴장일 관련 처리
 * 메모리, DB조회등의 다양한 방법이 있을 수 있음
 * @author macle
 */
public interface ClosedDays {

    /**
     * 휴장일여부 얻기
     * @param ymd yyyyMMdd
     * @return 휴장일 여부
     */
    boolean isClosedDay(String ymd);


    static String getCloseDaysFilePath(CountryCode countryCode) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return TradingConfig.getTradingDataPath() + fileSeparator + "market" + fileSeparator +"closed_days" + fileSeparator+  countryCode.toString() + "_closed_days.txt";
    }

    static String getLastTradeYmd(ClosedDays closedDays, String ymd){

        String tradeYmd = ymd;

        for(;;){
            if(closedDays.isClosedDay(tradeYmd)){
                tradeYmd = YmdUtil.getYmd(tradeYmd, -1);
                continue;
            }

            return tradeYmd;
        }

    }

}
