package io.runon.trading.system;

import com.seomse.commons.config.Config;
import com.seomse.commons.config.ConfigSet;
import com.seomse.commons.config.MapConfigData;
import com.seomse.jdbc.PrepareStatementData;
import com.seomse.jdbc.PrepareStatements;
import com.seomse.jdbc.objects.JdbcObjects;
import com.seomse.sync.SyncService;
import com.seomse.sync.Synchronizer;
import com.seomse.sync.SynchronizerManager;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public class RunonSystemManager implements Synchronizer {

    private static class Singleton {
        private static final RunonSystemManager instance = new RunonSystemManager();
    }

    public static RunonSystemManager getInstance(){
        return Singleton.instance;
    }

    private final MapConfigData systemConfigData = new MapConfigData(new HashMap<>());

    private RunonSystemManager(){
        //설정파일 불러오기
        Config.getConfig("");
        //동기화 등록
        SynchronizerManager.getInstance().add(this);

        systemConfigData.setPriority(Config.getInteger("config.db.priority", ConfigSet.XML_FILE_PRIORITY + 1));
        configSync();
        Config.addConfigData(systemConfigData);

        //동기화 서비스 실행
        new SyncService().start();

    }

    private long configUpdateTime = -1;

    @Override
    public void sync() {
        configSync();

    }

    public void configSync(){
        List<CommonConfig> configList;
        if(configUpdateTime > 0){
            configList = JdbcObjects.getObjList(CommonConfig.class, null, "updated_at asc");
        }else{

            Map<Integer, PrepareStatementData> prepareStatementDataMap =  PrepareStatements.newTimeMap(configUpdateTime);
            configList = JdbcObjects.getObjList(CommonConfig.class,  null ,"updated_at > ?", "updated_at asc", prepareStatementDataMap);
        }

        if(configList.size() > 0){

            for(CommonConfig commonConfig : configList){
                if(commonConfig.isDel()){
                    systemConfigData.remove(commonConfig.getKey());
                }else{
                    systemConfigData.put(commonConfig.getKey(), commonConfig.getValue());
                }
            }
            configUpdateTime =  configList.get(configList.size()-1).getUpdatedAt();
            configList.clear();
        }
    }

}
