package io.runon.trading.technical.analysis.indicators.market.mv;

import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.Disparity;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandleTimes;
import io.runon.trading.technical.analysis.volume.Volumes;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Market Trading Price Disparity
 * 0 ~ 1000
 * market.volume.disparity.max 설정값에 최대값은 변할 수 있음
 * 거래대금으로 보는 이격도
 * @author macle
 */
public class Mtpd extends Mvd{
    public Mtpd(SymbolCandleTimes symbolCandleTimes){
        super(symbolCandleTimes);
    }

    public Mtpd(SymbolCandle[] symbolCandles) {
        super(symbolCandles);
    }


    @Override
    public TimeNumber getData(int index) {

        int minCount = minAverageCount + 1;
        if(minCount > averageCount + 1) {
            minCount = averageCount + 1;
        }

        long time = times[index];

        int length = index + 1;
        if( length < minCount){
            return new TimeNumberData(time, BigDecimal.ZERO);
        }
        int avgStartIndex = times.length - index- averageCount -1;
        if(avgStartIndex < 0){
            avgStartIndex = 0;
        }

        long avgStartTime = times[avgStartIndex];

        int searchLength = searchIndex(index);
        int check = (times.length - index);

        BigDecimal avgSum = BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO;
        for(SymbolCandle symbolCandle : symbolCandles){
            TradeCandle[] candles = symbolCandle.getCandles();
            if(candles.length < minCount){
                continue;
            }

            int openTimeIndex = TaCandles.getOpenTimeIndex(candles, time, searchLength);
            if(openTimeIndex == -1){

                continue;
            }

            if(openTimeIndex +1 < minCount){

                continue;
            }

            int averageStartIndex =  TaCandles.getNearOpenTimeIndex(candles, avgStartTime, searchLength + (index - avgStartIndex));
            if (averageStartIndex == -1){

                continue;
            }

            if(averageStartIndex >= openTimeIndex){
                continue;
            }

            if(openTimeIndex - averageStartIndex +1 < minCount){
                continue;
            }

            TradeCandle candle = candles[openTimeIndex];
            if(minTradingPrice != null &&  candle.getTradingPrice().compareTo(minTradingPrice) < 0) {
                continue;
            }

            BigDecimal [] tradingPrices = Volumes.getTradingPrices(candles, averageStartIndex , openTimeIndex);
            Arrays.sort(tradingPrices);
            BigDecimal avg = BigDecimals.average(tradingPrices, highestExclusionRate);
            if(avg.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }

            sum = sum.add(candle.getTradingPrice());
            avgSum = avgSum.add(avg);

        }

        if(avgSum.compareTo(BigDecimal.ZERO) == 0|| sum.compareTo(BigDecimal.ZERO) == 0){
            return new TimeNumberData(time, BigDecimal.ZERO);
        }

        BigDecimal disparity = Disparity.get(sum, avgSum, scale);

        if(disparity.compareTo(maxDisparity) > 0){
            disparity = maxDisparity;
        }

        return new TimeNumberData(time, disparity);
    }
}
