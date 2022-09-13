package io.runon.trading.view;

import io.runon.trading.TimeNumber;
import lombok.Data;

/**
 * 라인 정보
 * @author ccsweets
 */
@Data
public class Lines {
    TimeNumber [] lines;
    String color = "black";
    int size = 2;
    boolean rightSide = true;
    boolean isValueVisible = true;

}
