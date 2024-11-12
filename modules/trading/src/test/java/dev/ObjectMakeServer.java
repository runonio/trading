package dev;

import com.seomse.commons.config.Config;
import com.seomse.jdbc.objects.JdbcObjects;
import com.seomse.jdbc.sync.JdbcSync;

import java.sql.Connection;
/**
 * @author macle
 */
public class ObjectMakeServer {
    public static void main(String[] args) {
        Config.getConfig("");
        String tableName = "category_key_value";

        try(Connection serverConn = JdbcSync.newSyncServerConnection()){
            System.out.println("class make info");
            System.out.println(JdbcObjects.makeObjectValue(serverConn, tableName, false));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
