package io.runon.trading;

import io.runon.commons.config.Config;
import io.runon.commons.config.ConfigSet;
import io.runon.commons.config.JsonFileProperties;
import io.runon.commons.config.JsonFilePropertiesManager;
import io.runon.commons.utils.ExceptionUtil;
import io.runon.trading.data.TradingDataPath;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;

/**
 * 트레이딩 기본 설정 모음
 * @author macle
 */
@Slf4j
public class TradingConfig {

    //텍스트 표시에 사용하는 기본설정
    public static final ZoneId DEFAULT_TIME_ZONE_ID = ZoneId.of(Config.getConfig("trading.view.time.zone.id","Asia/Seoul"));

    public static final String RUNON_API_ADDRESS = Config.getConfig("runon.api.address","https://api.runon.io");

    public static final long RUNON_API_SLEEP_TIME = Config.getLong("runon.api.sleep.time", 200L);

    public static final JsonFileProperties DEFAULT_JSON_PROPERTIES =  JsonFilePropertiesManager.getInstance().getByName(Config.getConfig("trading.json.properties.path", ConfigSet.CONFIG_DIR_PATH + "/trading_properties.json"));

    private static final String  TRADING_DATA_PATH = initTradingDataPath();
    public static String initTradingDataPath(){
        String dataPath = Config.getConfig("trading.data.path");
        if( dataPath != null){
            return TradingDataPath.changeSeparator(dataPath);
        }

        String defaultDir ;
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")){
            defaultDir = "D:\\runon\\data";
        }else{
            defaultDir = "/app/runon/data";
        }
        return defaultDir;
    }



    public static String getTradingDataPath(){
        return TRADING_DATA_PATH;
    }



    public static CountryCode getDefaultCountryCode(){
        try{
            return CountryCode.valueOf(Config.getConfig("trading.default.country.code","KOR"));
        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
        return CountryCode.KOR;
    }

    public static final String [] DEFAULT_API_CANDLE_DATA_PATHS = {
            "bonds"
            , "commodities"
            , "currencies"
            , "indices/futures"
    };


    public static String [] getApiCandleDataPaths(){


        String [] apiDataPaths = DEFAULT_JSON_PROPERTIES.getStrings("api_data_paths");

        if(apiDataPaths == null) {
            return DEFAULT_API_CANDLE_DATA_PATHS;
        }

        return apiDataPaths;
    }
}
