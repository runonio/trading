package io.runon.trading.data;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.time.Times;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.technical.analysis.candle.CandleTime;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.candles.TradeCandles;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * data
 * @author macle
 */
@Slf4j
public class CandleStorage {


    private final Map<Long, TradeCandles> candlesMap = new HashMap<>();

    public CandleStorage(){

    }

    public TradeCandles loadCandle(String path, long time){
        if(!FileUtil.isFile(path)){
            log.error("candle file not find: " + path);
            return null;
        }

        TradeCandle [] candles = CsvCandle.load(path, time);
        TradeCandles tradeCandles = new TradeCandles(time);
        tradeCandles.addCandle(candles);

        candlesMap.put(time, tradeCandles);

        return tradeCandles;
    }

    public TradeCandle[] getCandles(long time, long endTime, int count){
        return candlesMap.get(time).getCandles(endTime, count);
    }

    public static void main(String[] args) {
        CandleStorage candleStorage = new CandleStorage();
        TradeCandles tradeCandles = candleStorage.loadCandle("candle_15.csv", Times.MINUTE_15);
        TradeCandle [] candles = tradeCandles.getCandles();

        System.out.println(CandleTime.ymdhm(candles[candles.length-1].getOpenTime(), ZoneId.of("Asia/Seoul")));
        System.out.println(candles[candles.length-1].getOpenTime());

        long endTime = 1641861899999L;

        candles = tradeCandles.getCandles(endTime, 100);


        System.out.println(CandleTime.ymdhm(candles[candles.length-1].getOpenTime(), ZoneId.of("Asia/Seoul")));
        System.out.println(CandleTime.ymdhm(tradeCandles.getCandle(endTime).getOpenTime(), ZoneId.of("Asia/Seoul")));


    }
}
