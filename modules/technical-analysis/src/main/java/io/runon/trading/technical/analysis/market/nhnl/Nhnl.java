package io.runon.trading.technical.analysis.market.nhnl;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.BigDecimals;
import io.runon.trading.exception.CandleTimeException;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.market.MarketIndicator;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 신고가 신저가 지수
 * New High New Low
 * - 100 ~ 100
 *
 * @author macle
 */
public class Nhnl extends MarketIndicator<NhnlData> {

    /**
     * 캔들 정보를 활용한 분석
     */
    public Nhnl(SymbolCandle[] symbolCandles){
        super(symbolCandles);
    }


    private long candleTime = -1;
    private long searchTime = Times.DAY_1;
    private long timeRange = Times.WEEK_52;


    public void setCandleTime(long candleTime) {
        this.candleTime = candleTime;
    }

    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }

    public void setTimeRange(long timeRange) {
        this.timeRange = timeRange;
    }



    @Override
    public NhnlData getData(int index){

        if(candleTime < 1){
            throw new CandleTimeException("candle time error: " + candleTime);
        }

        long candleOpenTime = times[index];

        NhnlData data = new NhnlData();
        data.time = candleOpenTime;

        int validSymbolCount = 0;

        List<SymbolCandle> highList = new ArrayList<>();
        List<SymbolCandle> lowList = new ArrayList<>();

        long searchEndTime = candleOpenTime - searchTime + candleTime;
        long rangeEndTime = candleOpenTime - timeRange + candleTime;
        int searchLength = searchIndex(index);

        for(SymbolCandle symbolCandle : symbolCandles){
            TradeCandle [] candles = symbolCandle.getCandles();

            int openTimeIndex = TaCandles.getOpenTimeIndex(candles, data.time, searchLength);


            if(openTimeIndex == -1){
                continue;
            }

            //적어도 searchTime 의 2배 이상은 되어야함함
            long timeMax = candles[openTimeIndex].getCloseTime() * candles[0].getOpenTime();
            if(timeMax < searchTime * 2){
                continue;
            }

            BigDecimal high = candles[openTimeIndex].getHigh();
            BigDecimal low = candles[openTimeIndex].getLow();

            int rangeEndIndex = -1;

            for (int i = openTimeIndex-1; i > -1; i--) {
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

                for (int i = rangeEndIndex+1; i <= openTimeIndex ; i++) {
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
            return data;
        }

        data.length = validSymbolCount;

        if(highList.size() > 0){
            data.highs = highList.toArray(new SymbolCandle[0]);
            highList.clear();
        }

        if(lowList.size() > 0){
            data.lows = lowList.toArray(new SymbolCandle[0]);
            lowList.clear();
        }

        data.ratio = new BigDecimal(data.highs.length - data.lows.length).multiply(BigDecimals.DECIMAL_100).divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP).stripTrailingZeros();
        return data;
    }

    @Override
    public NhnlData[] newArray(int startIndex, int end) {
        NhnlData [] array = new NhnlData[end - startIndex];

        for (int i = 0; i < array.length ; i++) {
            array[i] = getData(i + startIndex);
        }
        return array;
    }
}
