package io.runon.trading;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author macle
 */
@Data
public class CandleData implements Candle{

    /**
     * 시가
     */
    protected BigDecimal open;

    /**
     * 종가
     */
    protected BigDecimal close;

    /**
     * 고가
     */
    protected BigDecimal high;

    /**
     * 저가
     */
    protected BigDecimal low;

}
