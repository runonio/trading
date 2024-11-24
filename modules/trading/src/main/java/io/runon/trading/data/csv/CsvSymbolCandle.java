package io.runon.trading.data.csv;

import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.IdCandleData;
import io.runon.trading.technical.analysis.candle.IdCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
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


    public CsvSymbolCandle(String path, String interval){
        this.path = path;
        this.interval = interval;
        candleTime = TradingTimes.getIntervalTime(interval);
    }


    public IdCandles[] load(long startTime, long endTime){
        //형지정
        String [] nullArray = null;

        //noinspection ConstantConditions
        return load(startTime,endTime, nullArray, null);
    }


    public IdCandles[] load(long startTime, long endTime, String startWith, String endWith){
        String [] startWiths = {startWith};
        String [] endWiths = {endWith};
        return load(startTime,endTime,startWiths,endWiths);
    }

    public IdCandles[] load(long startTime, long endTime, String [] startWiths, String [] endWiths){
        if(startTime == -1 || endTime == -1 || startTime >= endTime){
            throw new IllegalArgumentException("time error start time: " + startTime + ", end time: " + endTime);
        }


        File[] files = new File(path).listFiles();
        if(files == null){
            log.info("path file length 0: " + path);
            return IdCandles.EMPTY_ARRAY;
        }

        List<IdCandles> list  = new ArrayList<>();

        for(File file : files){
            if(!file.isDirectory()){
                continue;
            }

            String symbol = file.getName();
            if (startWiths != null) {
                boolean isLike = false;
                for(String startWith : startWiths){
                    if(symbol.startsWith(startWith)){
                        isLike =true;
                        break;
                    }
                }
                if(!isLike){
                   continue;
                }
            }

            if (endWiths != null) {
                boolean isLike = false;
                for(String endWith : endWiths){
                    if(symbol.endsWith(endWith)){
                        isLike =true;
                        break;
                    }
                }
                if(!isLike){
                    continue;
                }
            }


            TradeCandle[] candles = CsvCandle.load(file.getAbsolutePath() + "/" + interval, candleTime, startTime, endTime);

            if(candles.length == 0){
                continue;
            }

            list.add(new IdCandleData(symbol, candles));
        }

        if(list.size() == 0){
            return IdCandles.EMPTY_ARRAY;
        }

        IdCandles[] array = list.toArray(new IdCandles[0]);
        list.clear();
        return array;
    }

}
