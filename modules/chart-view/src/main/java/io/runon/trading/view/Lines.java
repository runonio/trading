package io.runon.trading.view;

import io.runon.trading.PriceOpenTime;
import lombok.Data;

/**
 * 라인 정보
 * @author ccsweets
 */
@Data
public class Lines {
    PriceOpenTime [] lines;
    String color = "black";
    int size = 2;
    boolean rightSide = true;
    boolean isValueVisible = true;


}
