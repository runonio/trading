package io.runon.trading.data.csv;

import io.runon.trading.data.TimeFileLineRead;
import io.runon.trading.technical.analysis.candle.TimeCandle;
import lombok.extern.slf4j.Slf4j;

/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
@Slf4j
public abstract class CsvTimeCandleRead extends TimeFileLineRead {

    protected final long time;
    public CsvTimeCandleRead(long time){
        this.time = time;
    }

    @Override
    public void addLine(String line){
        addTimeCandle(CsvCandle.makeTimeCandle(line, time));
    }

    public abstract void addTimeCandle(TimeCandle timeCandle);

}
