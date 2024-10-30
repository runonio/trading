package io.runon.trading.data;

import io.runon.trading.CountryUtils;
import io.runon.trading.TradingTimes;

import java.time.ZoneId;

/**
 * @author macle
 */
public class FuturesUtils {
    public static ZoneId getZoneId(Futures futures){
        String countryCode = CountryUtils.getCountryCode(futures.getFuturesId());
        return TradingTimes.getZoneId(countryCode);

    }
}
