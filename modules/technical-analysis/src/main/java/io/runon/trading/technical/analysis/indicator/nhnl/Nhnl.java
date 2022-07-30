package io.runon.trading.technical.analysis.indicator.nhnl;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.SymbolCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.exception.CandleTimeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 신고가 신저가 지수
 * New High New Low
 * @author macle
 */
public class Nhnl {

    public static final NhnlData NULL_DATA = new NullNhnlData();
    private SymbolCandle[] symbolCandles;

    /**
     * 캔들 정보를 활용한 분석
     */
    public Nhnl(SymbolCandle[] symbolCandles){
        setSymbolCandles(symbolCandles, true);
    }

    private int scale = 4;

    private long candleTime = -1;
    private long searchTime = Times.DAY_1;
    private long timeRange = Times.WEEK_52;

    private BigDecimal minTradingPrice = null;

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setCandleTime(long candleTime) {
        this.candleTime = candleTime;
    }

    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }

    public void setTimeRange(long timeRange) {
        this.timeRange = timeRange;
    }

    /**
     * 최소 가래대금 설정
     */
    public void setMinTradingPrice(BigDecimal minTradingPrice) {
        this.minTradingPrice = minTradingPrice;
    }

    public NhnlData getData(long candleOpenTime){

        if(candleTime < 1){
            throw new CandleTimeException("candle time error: " + candleTime);
        }

        int validSymbolCount = 0;

        List<SymbolCandle> highList = new ArrayList<>();
        List<SymbolCandle> lowList = new ArrayList<>();

        long searchEndTime = candleOpenTime - searchTime + candleTime;
        long rangeEndTime = candleOpenTime - timeRange + candleTime;

        for(SymbolCandle symbolCandle : symbolCandles){
            TradeCandle [] candles = symbolCandle.getCandles();

            int candleIndex = -1;
            for (int i = candles.length-1; i > -1; i--) {
                TradeCandle candle = candles[i];
                if(candle.getOpenTime() <= candleOpenTime){
                    candleIndex = i;
                    break;
                }
            }

            if(candleIndex == -1){
                continue;
            }

            //적어도 searchTime 의 2배 이상은 되어야함함
            long timeMax = candles[candleIndex].getCloseTime() * candles[0].getOpenTime();
            if(timeMax < searchTime * 2){
                continue;
            }

            BigDecimal high = candles[candleIndex].getHigh();
            BigDecimal low = candles[candleIndex].getLow();

            int rangeEndIndex = -1;

            for (int i = candleIndex-1; i > -1; i--) {
                TradeCandle candle = candles[i];
                if(candle.getCloseTime() < searchEndTime || candle.getOpenTime() < searchEndTime){
                    rangeEndIndex = i;
                    break;
                }
                if (candle.getHigh().compareTo(high) > 0){
                    high = candle.getHigh();
                }

                if(candle.getLow().compareTo(low) < 0){
                    low = candle.getLow();
                }
            }

            if(rangeEndIndex == -1){
                continue;
            }

            if(minTradingPrice != null) {
                BigDecimal tradingPrice = BigDecimal.ZERO;

                for (int i = rangeEndIndex+1; i <= candleIndex ; i++) {
                    tradingPrice = tradingPrice.add(candles[i].getTradingPrice());
                }

                if(tradingPrice.compareTo(minTradingPrice) < 0){
                    continue;
                }
            }

            BigDecimal rangeHigh = candles[rangeEndIndex].getHigh();
            BigDecimal rangeLow = candles[rangeEndIndex].getLow();
            for (int i = rangeEndIndex-1; i > -1; i--) {
                TradeCandle candle = candles[i];
                if(candle.getCloseTime() < rangeEndTime || candle.getOpenTime() < rangeEndTime){
                    break;
                }
                if (candle.getHigh().compareTo(rangeHigh) > 0){
                    rangeHigh = candle.getHigh();
                }

                if(candle.getLow().compareTo(rangeLow) < 0){
                    rangeLow = candle.getLow();
                }
            }

            if(high.compareTo(rangeHigh) > 0){
                highList.add(symbolCandle);
            }

            if(low.compareTo(rangeLow) < 0){
                lowList.add(symbolCandle);
            }

            validSymbolCount++;
        }



        if(validSymbolCount == 0){
            return NULL_DATA;
        }

        NhnlData nhnlData = new NhnlData();
        nhnlData.validSymbolCount = validSymbolCount;

        if(highList.size() > 0){
            nhnlData.highs = highList.toArray(new SymbolCandle[0]);
            highList.clear();
        }

        if(lowList.size() > 0){
            nhnlData.lows = lowList.toArray(new SymbolCandle[0]);
            lowList.clear();
        }

        nhnlData.index = new BigDecimal(nhnlData.highs.length - nhnlData.lows.length).multiply(BigDecimals.DECIMAL_100).divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP);
        return nhnlData;
    }


    public SymbolCandle[] getSymbolCandles() {
        return symbolCandles;
    }

    public void setSymbolCandles(SymbolCandle[] symbolCandles){
        setSymbolCandles(symbolCandles, true);
    }

    public void setSymbolCandles(SymbolCandle[] symbolCandles, boolean isCandleTimeUpdate) {
        this.symbolCandles = symbolCandles;

        if(isCandleTimeUpdate) {
            for (SymbolCandle symbolCandle : symbolCandles) {
                TradeCandle[] candles = symbolCandle.getCandles();
                if (candles.length > 2) {
                    break;
                }
                candleTime = candles[1].getTime() - candles[0].getTime();
            }
        }
    }


}
