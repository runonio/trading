package io.runon.trading.data;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.util.ArrayList;
import java.util.List;

/**
 * data
 * @author macle
 */
public class TimeFileCandles extends TimeFileLineRead {

    private TradeCandle [] candles;

    private final List<TradeCandle> list = new ArrayList<>();

    private final long time;

    public TimeFileCandles (long time){
        this.time = time;
    }

    @Override
    public void addLine(String line){
        list.add(CsvCandle.make(line, time));
    }

    @Override
    public void end(){
        candles = list.toArray(new TradeCandle[0]);
        list.clear();
    }

    public TradeCandle[] getCandles(){
        return candles;
    }

}
