package io.runon.trading.data.csv;

import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
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

    private final long time;

    public CsvCandle(long time){
        this.time = time;
    }

    //설정하지 않으면 기본값 활용
    private int candleCount = -1;

    public void setCandleCount(int candleCount) {
        if(candleCount < 1){
            throw new IllegalArgumentException("candle count > 0");
        }
        this.candleCount = candleCount;
    }

    private TradeCandles tradeCandles = null;

    public void setTradeCandles(TradeCandles tradeCandles) {
        this.tradeCandles = tradeCandles;
    }

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
                tradeCandles.addCandle(make(line, time));
            }
        }catch(IOException e){
            throw new IORuntimeException(e);
        }
        return tradeCandles;
    }

    public void out(String path){
        out(path, tradeCandles);
    }

    public void out(String path, TradeCandles tradeCandles){
        TradeCandle[] candles = tradeCandles.getCandles();

        if(candles.length == 0){
            throw new IllegalArgumentException("candles length > 0");
        }

        StringBuilder sb = new StringBuilder();
        for(TradeCandle candle : candles){
            sb.append("\n").append(value(candle));
        }

        FileUtil.fileOutput(sb.substring(1), path, false);
    }

    public static String value(TradeCandle tradeCandle){
        StringBuilder sb = new StringBuilder();
        sb.append(tradeCandle.getOpenTime());
        CsvCommon.append(sb, tradeCandle.getClose());
        CsvCommon.append(sb, tradeCandle.getOpen());
        CsvCommon.append(sb, tradeCandle.getHigh());
        CsvCommon.append(sb, tradeCandle.getLow());
        CsvCommon.append(sb, tradeCandle.getPrevious());
        CsvCommon.append(sb, tradeCandle.getVolume());
        CsvCommon.append(sb, tradeCandle.getTradingPrice());
        sb.append(",").append(tradeCandle.getTradeCount());
        CsvCommon.append(sb, tradeCandle.getBuyVolume());
        CsvCommon.append(sb, tradeCandle.getBuyTradingPrice());
        return sb.toString();
    }

    /**
     *
     * @param csv 캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10]
     * @param time 기준시간 일분봉등
     * @return TradeCandle
     */
    public static TradeCandle make(String csv, long time){
        String [] values = csv.split(",");
//                캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10]
        long openTime = Long.parseLong(values[0]);

        TradeCandle tradeCandle = new TradeCandle();
        tradeCandle.setOpenTime(openTime);
        tradeCandle.setCloseTime(openTime + time);
        tradeCandle.setClose(CsvCommon.getBigDecimal(values[1]));
        tradeCandle.setOpen(CsvCommon.getBigDecimal(values[2]));
        tradeCandle.setHigh(CsvCommon.getBigDecimal(values[3]));
        tradeCandle.setLow(CsvCommon.getBigDecimal(values[4]));
        tradeCandle.setPrevious(CsvCommon.getBigDecimal(values[5]));
        tradeCandle.setVolume(CsvCommon.getBigDecimal(values[6]));
        tradeCandle.setTradingPrice(CsvCommon.getBigDecimal(values[7]));
        if(values[8] != null){
            tradeCandle.setTradeCount(Integer.parseInt(values[8]));
        }

        tradeCandle.setBuyVolume(CsvCommon.getBigDecimal(values[9]));
        tradeCandle.setBuyTradingPrice(CsvCommon.getBigDecimal(values[10]));
        tradeCandle.setSellVolume();
        tradeCandle.setSellTradingPrice();
        //직전가로 변화량과 변화율 설정
        tradeCandle.setChange();
        return tradeCandle;
    }

}