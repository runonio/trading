package io.runon.trading.data;

import com.seomse.commons.config.Config;
import com.seomse.commons.config.JsonFileProperties;
import com.seomse.commons.config.JsonFilePropertiesManager;
import com.seomse.commons.exception.UndefinedException;

/**
 * @author macle
 */
public class TradingDataManager {

    private static class Singleton {
        private static final TradingDataManager instance = new TradingDataManager();
    }

    public static TradingDataManager getInstance(){
        return Singleton.instance;
    }


    private final JsonFileProperties jsonFileProperties;

    public final DataConnectType dataConnectType = DataConnectType.valueOf(Config.getConfig("io.runon.data.connect.type","db").toUpperCase());

    private final FuturesData futuresData;

    private TradingDataManager(){
        if(dataConnectType == DataConnectType.DB){
            futuresData = new FuturesJdbc();
        }else{
            //api 클래스들을 구현후에 다시 정의 형식에 의한 예지만 메모성 작성
//            stockData = new StockDataApi();
            throw new UndefinedException();
        }


        String jsonPropertiesName = "runon_trading.json";
        jsonFileProperties = JsonFilePropertiesManager.getInstance().getByName(jsonPropertiesName);
    }


    public DataConnectType getDataConnectType() {
        return dataConnectType;
    }

    public JsonFileProperties getJsonFileProperties() {
        return jsonFileProperties;
    }


    public FuturesData getFuturesData() {
        return futuresData;
    }
}
