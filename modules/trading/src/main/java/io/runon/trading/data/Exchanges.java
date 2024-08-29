package io.runon.trading.data;

import com.seomse.commons.exception.UndefinedException;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingTimes;
import io.runon.trading.exception.TradingDataException;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 * @author macle
 */
public class Exchanges {

    public static ZoneId getZoneId(String [] exchanges){

        Set<ZoneId> zoneIdSet = new HashSet<>();

        for(String exchange : exchanges){
            zoneIdSet.add(getZoneId(exchange));
        }

        if(zoneIdSet.size() != 1){
            return TradingTimes.UTC_ZONE_ID;
        }

        for(ZoneId zoneId : zoneIdSet){
            return zoneId;
        }

        return TradingTimes.UTC_ZONE_ID;
    }


    public static ZoneId getZoneId(String exchange){
        return switch (exchange) {
            case "KOSPI", "KOSDAQ", "KONEX" -> TradingTimes.KOR_ZONE_ID;
            case "NYSE", "NASDAQ", "NYSE_AMEX", "CME", "CBOT", "NYMEX", "COMEX", "CFD" -> TradingTimes.USA_ZONE_ID;
            case "SGX" -> TradingTimes.SGP_ZONE_ID;
            case "NSE" -> TradingTimes.INR_ZONE_ID;
            default -> TradingTimes.UTC_ZONE_ID;
        };

    }

    public static String [] getDefaultExchanges(CountryCode countryCode){
        if(countryCode == CountryCode.KOR){
            return new String[]{
                    "KOSPI"
                    , "KOSDAQ"
            };
        }else{
            throw new TradingDataException("undefined country: " + countryCode);
        }
    }

    public static String getOpenTimeYm(String exchange){
        return switch (exchange) {
            case "KOSPI", "KOSDAQ", "KONEX" -> "0900";
//            case "NYSE", "NASDAQ", "NYSE_AMEX", "CME", "CBOT", "NYMEX", "COMEX", "CFD" -> TradingTimes.USA_ZONE_ID;
//            case "SGX" -> TradingTimes.SGP_ZONE_ID;
//            case "NSE" -> TradingTimes.INR_ZONE_ID;
            default -> throw new UndefinedException();
        };
    }

}
