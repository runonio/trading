package io.runon.trading.data.time;

import lombok.Data;

import java.util.Comparator;

/**
 * @author macle
 */
@Data
public class LineTime {

    public static Comparator<LineTime> SORT = Comparator.comparingLong(o -> o.time);

    public LineTime(){

    }

    public LineTime(String line, long time){
        this.line = line;
        this.time = time;
    }

    String line;
    long time;
}