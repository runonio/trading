package io.runon.trading.data;

import io.runon.commons.exception.ReflectiveOperationRuntimeException;
import io.runon.commons.utils.DataCheck;
import io.runon.jdbc.objects.JdbcObjects;

/**
 * @author macle
 */
public class DailyDataUtils {

    public static boolean update(DailyData dailyData){
        try {
            String where = JdbcObjects.getCheckWhere(dailyData);

            DailyData lastData = JdbcObjects.getObj(DailyData.class, where);

            if(lastData == null){
                JdbcObjects.insert(dailyData);
                return true;
            }

            if(DataCheck.isEqualsObj(lastData.getDataValue(), dailyData.getDataValue())){
                return false;
            }

            dailyData.updatedAt = System.currentTimeMillis();
            JdbcObjects.update(dailyData, true);
            return true;

        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        }
    }

}
