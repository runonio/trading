package io.runon.trading.data;

import com.seomse.commons.utils.FileUtil;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.candles.TradeCandles;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * data
 * @author macle
 */
@Slf4j
public class CandleStorage {

    private final Map<Long, TradeCandles> candlesMap = new HashMap<>();

    public TradeCandles loadCandle(String path, long time){
        if(!FileUtil.isFile(path)){
            log.error("candle file not find: " + path);
            return null;
        }

        TradeCandle [] candles = CsvCandle.load(path, time);
        TradeCandles tradeCandles = new TradeCandles(time);
        tradeCandles.setCount(candles.length);
        tradeCandles.addCandle(candles);

        candlesMap.put(time, tradeCandles);

        return tradeCandles;
    }

    public TradeCandle[] getCandles(long time, long endTime, int count){
        return candlesMap.get(time).getCandles(endTime, count);
    }

}
