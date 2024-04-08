package io.runon.trading.data.file;

import lombok.Data;

import java.util.Comparator;

/**
 * @author macle
 */
@Data
public class LineTime {

    public static Comparator<LineTime> SORT = Comparator.comparingLong(o -> o.time);


    String line;
    long time;

    public LineTime(){

    }

    public LineTime(String line, long time){
        this.line = line;
        this.time = time;
    }

}
