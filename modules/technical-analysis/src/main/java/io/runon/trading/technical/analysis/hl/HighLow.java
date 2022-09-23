package io.runon.trading.technical.analysis.hl;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author macle
 */
@Data
public class HighLow {
    //고가 저가를 찾기 시작한 기준 시가
    long time;
    
    BigDecimal high;
    int highIndex;
    long highTime;

    BigDecimal low;
    int lowIndex;
    long lowTime;

}
