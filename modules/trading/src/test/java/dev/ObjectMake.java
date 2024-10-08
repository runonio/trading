package dev;

import com.seomse.commons.config.Config;
import com.seomse.jdbc.connection.ApplicationConnectionPool;
import com.seomse.jdbc.objects.JdbcObjects;

/**
 * @author macle
 */
public class ObjectMake {

    public static void main(String[] args) {

        Config.getConfig("");
        //noinspection ResultOfMethodCallIgnored
        ApplicationConnectionPool.getInstance();

        String tableName = "common_config";
        System.out.println("class make info");
        System.out.println(JdbcObjects.makeObjectValue(tableName, false));

    }
}
