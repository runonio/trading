package io.runon.trading.data.jdbc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seomse.commons.exception.ReflectiveOperationRuntimeException;
import com.seomse.commons.utils.GsonUtils;
import com.seomse.jdbc.exception.SQLRuntimeException;
import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.trading.TradingGson;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * trading 에서 사용하는 db관련 유틸성
 * @author macle
 */
@Slf4j
public class TradingJdbc {

    public static void updateTimeCheck(Object [] array) {
        for(Object o : array){
            updateTimeCheck(o);
        }
    }

    public static void updateTimeCheck(Object o) {
        try {
            String where = JdbcObjects.getCheckWhere(o);
            Object source = JdbcObjects.getObj(o.getClass(), where);

            if (source == null) {
                JdbcObjects.insert(o);
            } else {
                if (!TradingJdbc.equalsOutUpdatedAt(source, o)) {
                    GsonUtils.mergeJsonField("dataValue", source, o);
                    JdbcObjects.update(o, false);
                }
            }
        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        }
    }

    public static void updateTimeCheck(Connection conn, Object o) {
        try {
            String where = JdbcObjects.getCheckWhere(o);
            Object source = JdbcObjects.getObj(conn, o.getClass(), where);

            if (source == null) {
                JdbcObjects.insert(conn, o);
            } else {
                if (!TradingJdbc.equalsOutUpdatedAt(source, o)) {
                    GsonUtils.mergeJsonField("dataValue", source, o);
                    JdbcObjects.update(conn, o, false);
                }
            }
        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }


    public static boolean equalsOutUpdatedAt(Object source, Object target){

        String sourceOutUpdatedAt = getOutUpdateAtJson(source);
        JsonObject sourceObject = TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(sourceOutUpdatedAt, JsonObject.class);
        String [] sourceKeys = sourceObject.keySet().toArray(new String[0]);

        String targetSourceOutUpdatedAt = getOutUpdateAtJson(target);
        JsonObject targetObject =  TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(targetSourceOutUpdatedAt, JsonObject.class);
        Set<String> targetKeySet = targetObject.keySet();
        for(String sourceKey : sourceKeys){
            if(targetKeySet.contains(sourceKey)){
                continue;
            }
            sourceObject.remove(sourceKey);
        }
        sourceOutUpdatedAt = TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(sourceObject);
        return sourceOutUpdatedAt.equals(targetSourceOutUpdatedAt);

    }


    public static String getOutUpdateAtJson(Object object){
        Gson gson = TradingGson.LOWER_CASE_WITH_UNDERSCORES;
        String jsonText = gson.toJson(object);

        JsonObject jsonObject = gson.fromJson(jsonText, JsonObject.class);
        jsonObject.remove("updated_at");

        return gson.toJson(jsonObject);
    }

}
