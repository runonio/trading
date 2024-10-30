package io.runon.trading;

import java.time.ZoneId;

/**
 * 국가코드 정리
 * @author macle
 */
public class CountryUtils {

    public static String getCountryCode(String id){
        return id.substring(0, id.indexOf("_"));
    }

    public static ZoneId getZoneId(String countryCode){
        try{
            CountryCode c = CountryCode.valueOf(countryCode);
            return getZoneId(c);
        }catch (Exception ignore){
            return TradingTimes.UTC_ZONE_ID;
        }
    }
    public static ZoneId getZoneId(CountryCode countryCode){
        if(countryCode == CountryCode.KOR){
            return TradingTimes.KOR_ZONE_ID;
        }else if(countryCode == CountryCode.USA){
            return TradingTimes.USA_ZONE_ID;
        }else if(countryCode == CountryCode.SGP){
            return TradingTimes.SGP_ZONE_ID;
        }else if(countryCode == CountryCode.INR){
            return TradingTimes.INR_ZONE_ID;
        }
        return TradingTimes.UTC_ZONE_ID;
    }

}
