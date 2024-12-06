package io.runon.trading.data.candle;

import io.runon.commons.utils.FileUtil;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.TradeCandles;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 전체 캔들을 메모리에 올려놓고 사용하는방식
 * 시장지표를 사용하는환경에서는 사용을 추천하지 않는다.
 * data
 * @author macle
 */
@Slf4j
public class TimeCandleFileStorage {

    private final Map<Long, TradeCandles> candlesMap = new HashMap<>();

    public TradeCandles loadCandle(String path, long time){
        if(!FileUtil.isFile(path)){
            log.error("candle file not find: " + path);
            return null;
        }

        TradeCandle [] candles = CsvCandle.load(path, time);
        TradeCandles tradeCandles = new TradeCandles(time);
        tradeCandles.setCount(candles.length);
        tradeCandles.setCandles(candles);
        candlesMap.put(time, tradeCandles);

        return tradeCandles;
    }

    public TradeCandle[] getCandles(long time, long endTime, int count){
        return candlesMap.get(time).getCandles(endTime, count);
    }

    public TradeCandle[] getCandles(long time){
        return candlesMap.get(time).getCandles();
    }

    public TradeCandle[] getCandles(long time, int count){
        return candlesMap.get(time).getCandles(count);
    }


}
