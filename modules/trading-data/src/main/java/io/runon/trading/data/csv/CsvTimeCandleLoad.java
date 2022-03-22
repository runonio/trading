package io.runon.trading.data.csv;

import io.runon.trading.data.LineTimeDataLoad;
import io.runon.trading.technical.analysis.candle.TimeCandle;
import lombok.extern.slf4j.Slf4j;

/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
@Slf4j
public abstract class CsvTimeCandleLoad extends LineTimeDataLoad {

    protected final long time;
    public CsvTimeCandleLoad(long time){
        this.time = time;
    }


    @Override
    public void addLine(String line){
        addTimeCandle(CsvCandle.makeTimeCandle(line, time));
    }

    public abstract void addTimeCandle(TimeCandle timeCandle);

}
