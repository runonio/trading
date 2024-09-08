package io.runon.trading.data;

import com.seomse.commons.exception.ReflectiveOperationRuntimeException;
import com.seomse.commons.utils.DataCheck;
import com.seomse.jdbc.objects.JdbcObjects;

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
