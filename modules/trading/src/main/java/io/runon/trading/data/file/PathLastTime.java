package io.runon.trading.data.file;

import com.seomse.commons.exception.UndefinedException;

/**
 * @author macle
 */
public interface PathLastTime {

    PathLastTime CSV = new CsvPathLastTime();
    PathLastTime JSON = new JsonPathLastTime();

    long getLastTime(String path);

    static PathLastTime getPathLastTime(String type){
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
