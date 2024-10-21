package io.runon.trading.data;

import com.seomse.commons.exception.ReflectiveOperationRuntimeException;
import com.seomse.commons.utils.DataCheck;
import com.seomse.jdbc.objects.JdbcObjects;
/**
 * @author macle
 */
public class TimeTextUtils {
    public static boolean update(TimeText timeText){
        try {
            String where = JdbcObjects.getCheckWhere(timeText);

            TimeText lastData = JdbcObjects.getObj(TimeText.class, where);

            if(lastData == null){
                JdbcObjects.insert(timeText);
                return true;
            }

            if(DataCheck.isEqualsObj(lastData.getDataValue(), timeText.getDataValue())){
                return false;
            }

            timeText.updatedAt = System.currentTimeMillis();
            JdbcObjects.update(timeText, true);
            return true;

        }catch (ReflectiveOperationException e){
            throw new ReflectiveOperationRuntimeException(e);
        }
    }
}
