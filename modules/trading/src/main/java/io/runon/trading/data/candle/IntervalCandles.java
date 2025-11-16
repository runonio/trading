package io.runon.trading.data.candle;

import io.runon.commons.utils.time.YmdUtils;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.TradingDataPath;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.nio.file.FileSystems;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author macle
 */
public class IntervalCandles {
    final String absolutePath;

    final Map<String, TradeCandle []> intervalMap = new HashMap<>();

    final String fileSeparator = FileSystems.getDefault().getSeparator();


    final String relativePathPath;

    public IntervalCandles(String candlePath, ZoneId zoneId){
        this.absolutePath = TradingDataPath.getAbsolutePath(candlePath);
        this.relativePathPath = TradingDataPath.getRelativePath(candlePath);
    }

    public IntervalCandles(String candlePath){
        this.relativePathPath = TradingDataPath.getRelativePath(candlePath);
        this.absolutePath = TradingDataPath.getAbsolutePath(candlePath);
    }


    public void setCandle(String interval, long beginTime, long endTime){


        String filesDirPath = absolutePath + fileSeparator + interval;
        TradeCandle [] lastCandles = intervalMap.get(interval);
        long candleTime = TradingTimes.getIntervalTime(interval);
        TradeCandle [] candles = CsvCandle.load(filesDirPath, lastCandles, candleTime, beginTime, endTime);
        intervalMap.put(interval, candles);
    }

    public TradeCandle [] getCandles(String interval){
        return intervalMap.get(interval);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getRelativePathPath() {
        return relativePathPath;
    }


    public static void main(String[] args) {
        ZoneId zoneId = TradingTimes.KOR_ZONE_ID;

//        IntervalCandles   intervalCandles = new IntervalCandles(TradingDataPath.getFuturesCandleRelativePathPath("bonds","USA_30_year"));
        IntervalCandles   intervalCandles =  new IntervalCandles(TradingDataPath.getFuturesCandleRelativePathPath("commodities","USA_gold"));
        intervalCandles.setCandle("1d", YmdUtils.getTime(20000101, zoneId), YmdUtils.getTime(20101013, zoneId));

        TradeCandle []candles = intervalCandles.getCandles("1d");
        for(TradeCandle candle : candles){
            System.out.println(candle);
        }

    }
}
