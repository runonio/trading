package io.runon.trading.data.file;

import com.seomse.commons.exception.UndefinedException;

/**
 * @author macle
 */
public interface PathTimeLine extends PathLastTime, TimeLine{

    PathTimeLine CSV = new CsvPathTimeLine();
    PathTimeLine JSON = new JsonPathTimeLine();


    static PathTimeLine getPathTimeLine(String type){
        type = type.toUpperCase();
        if(type.equals("CSV")){
            return CSV;
        }else if(type.equals("JSON")){
            return JSON;
        }else{
            throw new UndefinedException("Only CSV, JSON are supported -> " + type);
        }
    }

}
