package io.runon.trading.data.json;

import io.runon.trading.data.file.TimeFiles;
import org.json.JSONObject;

/**
  * @author macle
 */
public class JsonTimeFile {

    public static long getTime(String jsonValue){
        JSONObject lineObj = new JSONObject(jsonValue);
        return lineObj.getLong("t");
    }



    public static long getLastTime(String path){

        String lastLine = TimeFiles.getLastLine(path);

        if(lastLine == null){
            return -1;
        }
        return getTime(lastLine);

    }
}
