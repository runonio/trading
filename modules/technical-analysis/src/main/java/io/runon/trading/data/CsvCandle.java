package io.runon.trading.data;

import com.seomse.commons.exception.IORuntimeException;
import com.seomse.commons.utils.FileUtil;
import io.runon.trading.exception.EmptyCandleException;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.candles.TradeCandles;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

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
            throw new RuntimeException("count > 0 ... " + candleCount);
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
                String [] values = line.split(",");
//                캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10]
                long openTime = Long.parseLong(values[0]);

                TradeCandle tradeCandle = new TradeCandle();
                tradeCandle.setOpenTime(openTime);
                tradeCandle.setCloseTime(openTime + time);
                tradeCandle.setClose(getBigDecimal(values[1]));
                tradeCandle.setOpen(getBigDecimal(values[2]));
                tradeCandle.setHigh(getBigDecimal(values[3]));
                tradeCandle.setLow(getBigDecimal(values[4]));
                tradeCandle.setPrevious(getBigDecimal(values[5]));
                tradeCandle.setVolume(getBigDecimal(values[6]));
                tradeCandle.setTradingPrice(getBigDecimal(values[7]));
                if(values[8] != null){
                    tradeCandle.setTradeCount(Integer.parseInt(values[8]));
                }

                tradeCandle.setBuyVolume(getBigDecimal(values[9]));
                tradeCandle.setBuyTradingPrice(getBigDecimal(values[10]));

                tradeCandle.setSellVolume();
                tradeCandle.setSellTradingPrice();

                //직전가로 변화량과 변화율 설정
                tradeCandle.setChange();
            }
        }catch(IOException e){
            throw new IORuntimeException(e);
        }
        return null;
    }

    public void out(String path){
        out(path, tradeCandles);
    }

    public void out(String path, TradeCandles tradeCandles){
        TradeCandle[] candles = tradeCandles.getCandles();

        if(candles.length == 0){
            throw new EmptyCandleException();
        }

        StringBuilder sb = new StringBuilder();
        for(TradeCandle candle : candles){
            sb.append("\n").append(value(candle));
        }

        FileUtil.fileOutput(sb.substring(1), path, false);
    }

    public String value(TradeCandle tradeCandle){
        StringBuilder sb = new StringBuilder();
        sb.append(tradeCandle.getOpenTime()).append(",").append(tradeCandle.getCloseTime());
        append(sb, tradeCandle.getClose());
        append(sb, tradeCandle.getOpen());
        append(sb, tradeCandle.getHigh());
        append(sb, tradeCandle.getLow());
        append(sb, tradeCandle.getPrevious());
        append(sb, tradeCandle.getVolume());
        append(sb, tradeCandle.getTradingPrice());
        sb.append(",").append(tradeCandle.getTradeCount());
        append(sb, tradeCandle.getBuyVolume());
        append(sb, tradeCandle.getBuyTradingPrice());
        return sb.toString();
    }

    private void append(StringBuilder sb, BigDecimal bigDecimal){
        sb.append(",");
        if(bigDecimal != null){
            sb.append(bigDecimal.stripTrailingZeros());
        }
    }

    private BigDecimal getBigDecimal(String value){
        if(value == null){
            return null;
        }
        return new BigDecimal(value);
    }

}