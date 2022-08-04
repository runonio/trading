package io.runon.trading.view;

import lombok.Data;

/**
 * 시간과 색
 * @author macle
 */
@Data
public class TimeColor {

    long time;
    String color;

    public TimeColor(){

    }

    public TimeColor(long time, String color){
        this.time = time;
        this.color = color;
    }


}
