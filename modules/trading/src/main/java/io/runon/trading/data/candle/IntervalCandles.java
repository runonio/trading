package io.runon.trading.data.candle;

import com.seomse.commons.utils.time.YmdUtil;
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

    ZoneId zoneId = TradingTimes.UTC_ZONE_ID;

    final String relativePathPath;

    public IntervalCandles(String candlePath, ZoneId zoneId){
        this.absolutePath = TradingDataPath.getAbsolutePath(candlePath);
        this.relativePathPath = TradingDataPath.getRelativePath(candlePath);
        this.zoneId = zoneId;
    }

    public IntervalCandles(String candlePath){
        this.relativePathPath = TradingDataPath.getRelativePath(candlePath);
        this.absolutePath = TradingDataPath.getAbsolutePath(candlePath);
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public void setCandle(String interval, long beginTime, long endTime){


        String filesDirPath = absolutePath + fileSeparator + interval;
        System.out.println(filesDirPath);
        TradeCandle [] lastCandles = intervalMap.get(interval);
        long candleTime = TradingTimes.getIntervalTime(interval);
        TradeCandle [] candles = CsvCandle.load(filesDirPath, lastCandles, candleTime, beginTime, endTime, zoneId);
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

        IntervalCandles   vixCandles = new IntervalCandles(TradingDataPath.getFuturesCandleRelativePathPath("indices","USA_snp_500_vix"));



        vixCandles.setCandle("1d", YmdUtil.getTime(20241001, zoneId), YmdUtil.getTime(20241013, zoneId));

        TradeCandle []candles = vixCandles.getCandles("1d");
        for(TradeCandle candle : candles){
            System.out.println(candle);
        }

    }
}
