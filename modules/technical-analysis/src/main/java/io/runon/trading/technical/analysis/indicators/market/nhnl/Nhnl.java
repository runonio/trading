package io.runon.trading.technical.analysis.indicators.market.nhnl;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.BigDecimals;
import io.runon.trading.exception.CandleTimeException;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.market.MarketIndicators;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandleTimes;

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
public class Nhnl extends MarketIndicators<NhnlData> {

    /**
     * 캔들 정보를 활용한 분석
     */
    public Nhnl(SymbolCandle[] symbolCandles){
        super(symbolCandles);
    }

    public Nhnl(SymbolCandleTimes symbolCandleTimes) {
        super(symbolCandleTimes);
    }

    private long candleTime = -1;
    private long timeRange = Times.WEEK_52;


    public void setCandleTime(long candleTime) {
        this.candleTime = candleTime;
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

        long rangeEndTime = candleOpenTime - timeRange + candleTime;
        int searchLength = searchIndex(index);

        for(SymbolCandle symbolCandle : symbolCandles){
            TradeCandle [] candles = symbolCandle.getCandles();

            int openTimeIndex = TaCandles.getOpenTimeIndex(candles, data.time, searchLength);


            if(openTimeIndex < 1 ){
                continue;
            }

            //적어도 searchTime 의 2배 이상은 되어야함함

            TradeCandle candle = candles[openTimeIndex];

            if(minTradingPrice != null &&  candle.getTradingPrice().compareTo(minTradingPrice) < 0) {
                continue;
            }

            int rangeEndIndex = openTimeIndex-1;

            BigDecimal rangeHigh = candles[rangeEndIndex].getHigh();
            BigDecimal rangeLow = candles[rangeEndIndex].getLow();
            for (int i = rangeEndIndex - 1; i > -1; i--) {
                TradeCandle rangeCandle = candles[i];
                if(rangeCandle.getCloseTime() < rangeEndTime || rangeCandle.getOpenTime() < rangeEndTime){
                    break;
                }
                if (rangeCandle.getHigh().compareTo(rangeHigh) > 0){
                    rangeHigh = candle.getHigh();
                }

                if(rangeCandle.getLow().compareTo(rangeLow) < 0){
                    rangeLow = candle.getLow();
                }
            }

            if(candle.getHigh().compareTo(rangeHigh) > 0){
                highList.add(symbolCandle);
            }

            if(candle.getLow().compareTo(rangeLow) < 0){
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
        NhnlData[] array = new NhnlData[end - startIndex];

        for (int i = 0; i < array.length ; i++) {
            array[i] = getData(i + startIndex);
        }
        return array;
    }
}
