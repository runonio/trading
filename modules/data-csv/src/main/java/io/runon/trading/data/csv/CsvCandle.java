package io.runon.trading.data.csv;

import com.seomse.commons.exception.IORuntimeException;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.candles.TradeCandles;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class CsvCandle {


    private final long time ;

    public CsvCandle(long time){
        this.time = time;
    }

    //설정하지 않으면 기본값 활용
    private int candleCount = -1;

    public void setCandleCount(int candleCount) {
        if(candleCount < 1){
            throw new RuntimeException("count > 0 ... " + candleCount);
        }
        this.candleCount = candleCount;
    }


    private TradeCandles tradeCandles = null;

    public TradeCandles load(String filePath){
        if(tradeCandles == null){
            tradeCandles = new TradeCandles(time);
        }
        if(candleCount != -1){
            tradeCandles.setCount(candleCount);
        }


        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))){
            String line;
            while ((line = br.readLine()) != null) {
                String [] values = line.split(",");

                TradeCandle tradeCandle = new TradeCandle();

            }
        }catch(IOException e){
            throw new IORuntimeException(e);
        }






        return null;
    }



}
