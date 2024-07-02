package io.runon.trading.data.file;

import com.seomse.commons.exception.UndefinedException;

/**
 * 시계열 라인데이터 구조
 * @author macle
 */
public interface TimeLine {

    TimeLine CSV = new CsvTimeLine();
    TimeLine JSON = new JsonTimeLine();

    long getTime(String line);

    static TimeLine getTimeLine(String type){
        type = type.toUpperCase();
        if(type.equals("CSV")){
            return CSV;
        }else if(type.equals("JSON")){
            return JSON;
        }else{
            throw new UndefinedException("Only csv and json are supported -> " + type);
        }
    }

}
