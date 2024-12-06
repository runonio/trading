package io.runon.trading.data.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.runon.commons.utils.FileUtil;
import io.runon.trading.TradingGson;
import io.runon.trading.data.TextLong;
import io.runon.trading.data.file.TimeFiles;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    public static TextLong [] getLastTimeLines(String filePath){

        if(!FileUtil.isFile(filePath)){
            return new TextLong[0];
        }

        JsonArray jsonArray = TradingGson.PRETTY.fromJson(FileUtil.getFileContents(filePath, StandardCharsets.UTF_8), JsonArray.class);

        TextLong [] timeLines = new TextLong[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {

            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            TextLong timeLine = new TextLong();
            timeLine.setNumber(jsonObject.get("t").getAsLong());
            timeLine.setText(jsonObject.get("id").getAsString());
            jsonObject.get("t").getAsLong();
            timeLines[i] = timeLine;
        }


        return timeLines;
    }
    public static void updateLastTimeLines(TextLong[] idTimes, String filePath, Comparator<TextLong> sort){

        TextLong [] array;
        if(FileUtil.isFile(filePath)){
            //기존데이터에 새로 누적해서 정렬
            TextLong [] lastArray = getLastTimeLines(filePath);
            Map<String, TextLong> map = new HashMap<>();
            for(TextLong last : lastArray){
                map.put(last.getText(), last);
            }

            for(TextLong idTime : idTimes){
                map.put(idTime.getText(), idTime);
            }
            
            array = map.values().toArray(new TextLong[0]);

        }else{
            array = idTimes;
        }
        
        Arrays.sort(array, sort);
        JsonArray jsonArray = new JsonArray();

        for(TextLong idTime : array){
            JsonObject object = new JsonObject();
            object.addProperty("id", idTime.getText());
            object.addProperty("t", idTime.getNumber());
            jsonArray.add(object);

        }
        FileUtil.fileOutput(TradingGson.PRETTY.toJson(jsonArray), filePath, false);
    }
            

}
