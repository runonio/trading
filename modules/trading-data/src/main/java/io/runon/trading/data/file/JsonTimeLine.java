package io.runon.trading.data.file;

import org.json.JSONObject;

/**
 * 문자열을 활용한 lock 관리
 * @author macle
 */
public class JsonTimeLine implements TimeLine {

    @Override
    public long getTime(String line) {
        JSONObject lineObj = new JSONObject(line);
        return lineObj.getLong("t");
    }
}
