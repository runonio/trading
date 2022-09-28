package example;

import io.runon.trading.CandleTimes;
import io.runon.trading.data.csv.CsvTimeName;
/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class CsvCandleFileNameExample {
    public static void main(String[] args) {
//        TimeZone zone = TimeZone.ad

//        ZoneId zoneId = ZoneId.of("US/Eastern");
//        System.out.println(zoneId.getId());

        //기본은 미국장 (뉴욕)시간을 사용함
        System.out.println(CsvTimeName.getName(System.currentTimeMillis() , 5000));

        System.out.println(CsvTimeName.getName(System.currentTimeMillis(), 1000, CandleTimes.US_STOCK_ZONE_ID));
        System.out.println(CsvTimeName.getName(System.currentTimeMillis() , 1000, CandleTimes.KOREAN_STOCK_ZONE_ID));

    }
}
