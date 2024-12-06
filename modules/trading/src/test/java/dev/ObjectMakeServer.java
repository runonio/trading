package dev;

import io.runon.commons.config.Config;
import io.runon.jdbc.objects.JdbcObjects;
import io.runon.jdbc.sync.JdbcSync;

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
