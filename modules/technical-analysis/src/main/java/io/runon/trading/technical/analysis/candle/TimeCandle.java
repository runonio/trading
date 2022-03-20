package io.runon.trading.technical.analysis.candle;

import lombok.Data;

/**
 * 시간과 캔들값
 * @author macle
 */
@Data
public class TimeCandle {

    private long time;
    private TradeCandle candle;

    public TimeCandle(){}

    public TimeCandle(long time, TradeCandle candle){
        this.time = time;
        this.candle = candle;
    }

}
