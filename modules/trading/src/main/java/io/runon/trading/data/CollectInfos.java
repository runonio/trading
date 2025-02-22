package io.runon.trading.data;

import io.runon.commons.exception.ReflectiveOperationRuntimeException;
import io.runon.commons.utils.string.Check;
import io.runon.jdbc.JdbcQuery;
import io.runon.jdbc.exception.SQLRuntimeException;
import io.runon.jdbc.objects.JdbcObjects;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author macle
 */
public class CollectInfos {

    public static void updateTime(String collectId, String dataKey){
        JdbcQuery.execute("update collect_info set collected_at = CURRENT_TIMESTAMP where collect_id='" + collectId + "' and data_key='" + dataKey + "'");
    }

    public static Map<String, String> getInfoMap(Connection conn, String collectId){
        try {
            List<CollectInfo> collectInfoList = JdbcObjects.getObjList(conn, CollectInfo.class, "collect_id='" + collectId + "'");
            Map<String, String> map = new HashMap<>();
            for(CollectInfo collectInfo : collectInfoList){
                String ymdValue = collectInfo.getCollectInfo();

                if(collectInfo.getCollectInfo() == null ){
                    continue;
                }

                map.put(collectInfo.getDataKey(), ymdValue);
            }

            return map;
        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public static Map<String, String> getInfoYmdMap(Connection conn, String collectId){
        try {
            List<CollectInfo> collectInfoList = JdbcObjects.getObjList(conn, CollectInfo.class, "collect_id='" + collectId + "'");
            Map<String, String> map = new HashMap<>();
            for(CollectInfo collectInfo : collectInfoList){
                String ymdValue = collectInfo.getCollectInfo();
                if(collectInfo.getCollectInfo() == null || ymdValue.length() != 8 || !Check.isNumber(ymdValue)){
                    continue;
                }
                map.put(collectInfo.getDataKey(), ymdValue);
            }
            return map;
        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    public static void update(CollectInfo collectInfo){
        try {
            String where = JdbcObjects.getCheckWhere(collectInfo);
            CollectInfo source = JdbcObjects.getObj(CollectInfo.class, where);
            if(source == null){
                JdbcObjects.insert( collectInfo);
            }else{
                if(!Check.equals(collectInfo.getCollectInfo(), source.getCollectInfo())){
                    JdbcObjects.update( collectInfo, true);
                }
            }
        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        }
    }

    public static void update(Connection conn, CollectInfo collectInfo){
        try {
            String where = JdbcObjects.getCheckWhere(collectInfo);
            CollectInfo source = JdbcObjects.getObj(conn , CollectInfo.class, where);
            if(source == null){
                JdbcObjects.insert(conn, collectInfo);
            }else{
                if(!Check.equals(collectInfo.getCollectInfo(), source.getCollectInfo())){
                    JdbcObjects.update(conn, collectInfo, true);
                }
            }
        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }
}
