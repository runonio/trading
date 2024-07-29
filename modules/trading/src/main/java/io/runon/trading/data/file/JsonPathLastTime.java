package io.runon.trading.data.file;

import io.runon.trading.data.json.JsonTimeFile;
/**
 * @author macle
 */
public class JsonPathLastTime implements PathLastTime{
    @Override
    public long getLastTime(String path) {
        return JsonTimeFile.getLastTime(path);
    }

    @Override
    public String toString(){
        return "JSON";
    }
}
