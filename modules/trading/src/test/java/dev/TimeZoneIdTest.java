package dev;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingTimes;

/**
 * @author macle
 */
public class TimeZoneIdTest {
    public static void main(String[] args) {
        String zoneTest=  Times.ymdhm(System.currentTimeMillis(), TradingTimes.INR_ZONE_ID);
        System.out.println(zoneTest);
    }
}
