package io.runon.trading.data;

import io.runon.trading.CountryCode;
/**
 * @author macle
 */
public interface FuturesPathLastTime {



    long getLastTime(Futures stock, String interval);

    String getFilesDirPath(Futures stock, String interval);

    String getLastTimeFilePath(CountryCode countryCode, String interval);
}
