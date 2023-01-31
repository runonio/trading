package example;

import com.seomse.commons.utils.time.DateUtil;
import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingTimes;

import java.time.Instant;
import java.time.ZonedDateTime;
/**
 * 캔들 파일 새로운 표준경로로 이동
 * @author macle
 */
public class TimeText {
    public static void main(String[] args) {

        long time = 1648927560000L;

        Instant i = Instant.ofEpochMilli(time);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(i, TradingTimes.UTC_ZONE_ID);
        System.out.println(zonedDateTime.getYear() +  DateUtil.getDateText(zonedDateTime.getMonthValue()));
        System.out.println(Times.ymdhm(time, TradingTimes.UTC_ZONE_ID ));
    }
}
