package io.runon.trading.data.file;

import io.runon.trading.data.json.JsonTimeFile;

/**
 * 문자열을 활용한 lock 관리
 * @author macle
 */
public class JsonPathTimeLine implements PathTimeLine{
    @Override
    public long getLastTime(String path) {
        return JsonTimeFile.getLastTime(path);
    }

    @Override
    public long getTime(String line) {
        return JsonTimeFile.getTime(line);
    }

    @Override
    public String toString(){
        return "JSON";
    }
}
