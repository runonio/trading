package io.runon.trading.data.file;

import io.runon.trading.data.json.JsonTimeFile;

/**
 * 문자열을 활용한 lock 관리
 * @author macle
 */
public class JsonTimeLine implements TimeLine {

    @Override
    public long getTime(String line) {
        return JsonTimeFile.getTime(line);
    }
}
