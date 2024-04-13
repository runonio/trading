package io.runon.trading;

import com.seomse.commons.config.Config;

import java.time.ZoneId;

/**
 * 트레이딩 기본 설정 모음
 * @author macle
 */
public class TradingConfig {

    //텍스트 표시에 사용하는 기본설정
    public static final ZoneId VIEW_TIME_ZONE_ID = ZoneId.of(Config.getConfig("trading.view.time.zone.id","Asia/Seoul"));

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

}
