package io.runon.trading.technical.analysis.indicators.market.mv;

import com.seomse.commons.config.Config;
import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.Disparity;
import io.runon.trading.technical.analysis.indicators.market.MarketIndicators;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandleTimes;
import io.runon.trading.technical.analysis.volume.Volumes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * Market Volume Disparity
 * 0 ~ 1000
 * market.volume.disparity.max 설정값에 최대값은 변할 수 있음
 * @author macle
 */
public class Mvd extends MarketIndicators<TimeNumber> {

    protected int averageCount =  Config.getInteger("volume.average.count", 50);

    protected int minAverageCount = Config.getInteger("volume.average.min.count", 10);

    protected BigDecimal highestExclusionRate = new BigDecimal(Config.getConfig("volume.average.default.highest.exclusion.rate", "0.1"));

    protected BigDecimal maxDisparity = new BigDecimal(Config.getConfig("market.volume.disparity.max", "1000"));

    public void setAverageCount(int averageCount) {
        this.averageCount = averageCount;
    }

    public void setMinAverageCount(int minAverageCount) {
        this.minAverageCount = minAverageCount;
    }

    public void setHighestExclusionRate(BigDecimal highestExclusionRate) {
        this.highestExclusionRate = highestExclusionRate;
    }

    public void setMaxDisparity(BigDecimal maxDisparity) {
        this.maxDisparity = maxDisparity;
    }

    public Mvd(SymbolCandle[] symbolCandles){
        super(symbolCandles);
        scale = 2;
    }
    public Mvd(SymbolCandleTimes symbolCandleTimes){
        super(symbolCandleTimes);
        scale = 2;
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

        int validSymbolCount = 0;

        int searchLength = searchIndex(index);
        int check = (times.length - index);

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

            BigDecimal [] volumes = Volumes.getVolumes(candles, averageStartIndex , openTimeIndex);
            Arrays.sort(volumes);
            BigDecimal avg = BigDecimals.average(volumes, highestExclusionRate);
            if(avg.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }

            validSymbolCount++;


            TradeCandle tradeCandle = candles[openTimeIndex];
            BigDecimal disparity = Disparity.get(tradeCandle.getVolume(), avg);

            if(disparity.compareTo(maxDisparity) > 0){
                disparity = maxDisparity;
            }
            sum = sum.add(disparity);
        }

        if(validSymbolCount == 0 || sum.compareTo(BigDecimal.ZERO) == 0){
            return new TimeNumberData(time, BigDecimal.ZERO);
        }

        return new TimeNumberData(time, sum.divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP));
    }

    @Override
    public TimeNumber[] newArray(int startIndex, int end) {
        TimeNumber[] array = new TimeNumber[end - startIndex];

        for (int i = 0; i < array.length ; i++) {
            array[i] = getData(i + startIndex);
        }
        return array;
    }
}
