package io.runon.trading.data.management;

import com.seomse.jdbc.objects.JdbcObjects;
import io.runon.trading.data.Futures;
import io.runon.trading.data.TradingDataPath;
import io.runon.trading.data.csv.CsvTimeFile;

import java.nio.file.FileSystems;

/**
 * @author macle
 */
public class FuturesPathLastTimeCandle implements FuturesPathLastTime{
    @Override
    public long getLastTime(Futures futures, String interval) {
        return CsvTimeFile.getLastTime(getFilesDirPath(futures, interval));
    }

    @Override
    public String getFilesDirPath(Futures futures, String interval) {
        return TradingDataPath.getFuturesCandleFilesPath(futures,interval);
    }

    @Override
    public String getLastTimeFilePath(Futures futures, String interval) {

        String fileSeparator = FileSystems.getDefault().getSeparator();
        return  TradingDataPath.getFuturesCandlePath(futures)+fileSeparator+futures.getFuturesId()+fileSeparator+"last_time_" + interval+".txt";
    }

    public static void main(String[] args) {
        Futures futures = JdbcObjects.getObj(Futures.class , "futures_id='KOR_201VB212'" );
        FuturesPathLastTime candle = FuturesPathLastTime.CANDLE;
        System.out.println(candle.getLastTimeFilePath(futures, "1d"));

    }

}
