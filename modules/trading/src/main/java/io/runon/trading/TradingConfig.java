package io.runon.trading;

import com.seomse.commons.config.Config;
import com.seomse.commons.config.ConfigSet;
import com.seomse.commons.config.JsonFileProperties;
import com.seomse.commons.config.JsonFilePropertiesManager;
import com.seomse.commons.utils.ExceptionUtil;
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

    public static final JsonFileProperties DEFAULT_JSON_PROPERTIES =  JsonFilePropertiesManager.getInstance().getByName(Config.getConfig("trading.json.properties.path", ConfigSet.CONFIG_DIR_PATH + "/trading_properties.json"));

    public static String getTradingDataPath(){

        String dataPath = Config.getConfig("trading.data.path");
        if( dataPath != null){
            return dataPath;
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

    public static CountryCode getDefaultCountryCode(){
        try{
            return CountryCode.valueOf(Config.getConfig("trading.default.country.code","KOR"));
        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
        return CountryCode.KOR;
    }

    private static int TRADING_THREAD_COUNT= -1;

    /**
     * 설정하지 않으면 전체 쓰레드수 -1
     * 16코어라면 15개를 사용한다
     * 설정은 그 이하로만 설정할 수 있다
     * @return 병렬처리에 사용하는 기본 쓰레드 수
     */
    public static int getTradingThreadCount(){

        if(TRADING_THREAD_COUNT > 0){
            return TRADING_THREAD_COUNT;
        }

        int maxThreadCount =  Runtime.getRuntime().availableProcessors() -1 ;
        if(maxThreadCount < 1){
            maxThreadCount = 1;
        }

        Integer config = Config.getInteger("trading.thread.count");
        if(config != null && config > 0){
            TRADING_THREAD_COUNT = config;

            if(TRADING_THREAD_COUNT > maxThreadCount){
                TRADING_THREAD_COUNT = maxThreadCount;
            }

            return TRADING_THREAD_COUNT;
        }

        TRADING_THREAD_COUNT = maxThreadCount;
        return TRADING_THREAD_COUNT;
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
