package io.runon.trading.data.csv;

import io.runon.trading.CandleTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandleData;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 데이터 로드
 * @author macle
 */
@Slf4j
public class CsvSymbolCandle {
    //캔들파일 경로
    private final String path;
    private final String interval;

    private final long candleTime;

    private ZoneId zoneId = CandleTimes.US_STOCK_ZONE_ID;

    public CsvSymbolCandle(String path, String interval){
        this.path = path;
        this.interval = interval;
        candleTime = CandleTimes.getIntervalTime(interval);
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public SymbolCandle [] read(long startTime, long endTime){
        if(startTime == -1 || endTime == -1 || startTime >= endTime){
            throw new IllegalArgumentException("time error start time: " + startTime + ", end time: " + endTime);
        }

        String startName = CsvTimeName.getName(startTime , candleTime, zoneId);
        String endName = CsvTimeName.getName(startTime , candleTime, zoneId);

        int startFileNum = Integer.parseInt(startName);
        int endFileNum = Integer.parseInt(endName);

        File[] files = new File(path).listFiles();
        if(files == null){
            log.info("path file length 0: " + path);
            return SymbolCandle.EMPTY_ARRAY;
        }

        List<SymbolCandle> list  = new ArrayList<>();

        for(File file : files){
            if(!file.isDirectory()){
                continue;
            }

            String symbol = file.getName();
            TradeCandle[] candles = CsvCandle.load(file.getAbsolutePath() + "/" + interval, candleTime, startTime, endTime, zoneId);
            if(candles.length == 0){
                continue;
            }

            list.add(new SymbolCandleData(symbol, candles));
        }

        if(list.size() == 0){
            return SymbolCandle.EMPTY_ARRAY;
        }

        SymbolCandle [] array = list.toArray(new SymbolCandle[0]);

        return SymbolCandle.EMPTY_ARRAY;
    }

}
