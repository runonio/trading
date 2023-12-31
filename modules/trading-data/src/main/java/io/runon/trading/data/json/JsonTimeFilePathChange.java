package io.runon.trading.data.json;

import io.runon.trading.data.file.TimeFilePathChange;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * json 형식
 * @author macle
 */
@Slf4j
public class JsonTimeFilePathChange extends TimeFilePathChange {
    private String timeKey ="t";


    public void setTimeKey(String timeKey) {
        this.timeKey = timeKey;
    }

    @Override
    public long getTime(String line) {
        try {
            JSONObject lineObj = new JSONObject(line);
            return lineObj.getLong(timeKey);
        }catch (org.json.JSONException e){
            log.error(line);
            throw e;
        }

    }
}
