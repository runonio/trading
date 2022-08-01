package io.runon.trading.technical.analysis.indicator.volume;

import com.seomse.commons.config.Config;
import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.SymbolCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.indicator.Disparity;
import io.runon.trading.technical.analysis.volume.Volumes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 거래량이 급등하는 종목의 수를 지수화 함
 * 0 ~ 100
 * @author macle
 */
public class SoaringTradingVolume {


    private SymbolCandle[] symbolCandles;

    private int averageCount = 50;

    private int minAverageCount = 10;
    private int scale = 4;
    public void setScale(int scale) {
        this.scale = scale;
    }
    private BigDecimal highestExclusionRate = new BigDecimal(Config.getConfig("soaring.trading.volume.default.highest.exclusion.rate", "0.1"));

    private BigDecimal disparity = new BigDecimal(Config.getConfig("soaring.trading.volume.default.highest.exclusion.rate", "300"));

    public SoaringTradingVolume(SymbolCandle[] symbolCandles){
        setSymbolCandles(symbolCandles);
    }

    private long [] times = null;
    public void setSymbolCandles(SymbolCandle[] symbolCandles) {
        this.symbolCandles = symbolCandles;

    }

    public void setTimes(){
        times = TaCandles.getTimes(symbolCandles);
    }

    public void setTimes(long[] times) {
        this.times = times;
    }

    public void setAverageCount(int averageCount) {
        this.averageCount = averageCount;
    }

    public void setMinAverageCount(int minAverageCount) {
        this.minAverageCount = minAverageCount;
    }

    public void setHighestExclusionRate(BigDecimal highestExclusionRate) {
        this.highestExclusionRate = highestExclusionRate;
    }

    public SoaringTradingVolumeData getData(SymbolCandle[] symbolCandles){
        setSymbolCandles(symbolCandles);
        return getData();
    }

    public void setDisparity(BigDecimal disparity) {
        this.disparity = disparity;
    }

    public SoaringTradingVolumeData getData(){
        return getData(times.length-1);
    }

    public SoaringTradingVolumeData getData(int index){
        if(times == null){
            setTimes();
        }

        int minCount = minAverageCount + 1;
        if(minCount > averageCount + 1){
            minCount = averageCount + 1 ;
        }
        SoaringTradingVolumeData data = new SoaringTradingVolumeData();
        data.time = times[index];
        int length = index + 1;
        if( length < minCount){
            return data;
        }

        int avgStartIndex = times.length - averageCount -1;
        if(avgStartIndex < 0){
            avgStartIndex = 0;
        }

        long avgStartTime = times[avgStartIndex];

        int validSymbolCount = 0;

        List<SymbolCandle> list = new ArrayList<>();

        int searchLength = (times.length - index) + averageCount;

        for(SymbolCandle symbolCandle : symbolCandles){
            TradeCandle[] candles =symbolCandle.getCandles();
            if(candles.length < minCount){
                continue;
            }

            int openTimeIndex = TaCandles.getOpenTimeIndex(candles, data.time, searchLength);
            if(openTimeIndex == -1){
                continue;
            }

            if(openTimeIndex +1 < minCount){
                continue;
            }

            int averageStartIndex =  TaCandles.getStartIndex(candles, avgStartTime, searchLength);
            if (averageStartIndex == -1){
                continue;
            }

            if(averageStartIndex >= openTimeIndex){
                continue;
            }

            if(openTimeIndex - averageStartIndex +1 < minCount){
                continue;
            }

            BigDecimal [] volumes = Volumes.getVolumes(candles, averageStartIndex , openTimeIndex);
            Arrays.sort(volumes);
            BigDecimal avg = BigDecimals.average(volumes, highestExclusionRate);
            if(avg.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }

            validSymbolCount++;
            BigDecimal d = Disparity.get(candles[candles.length-1].getVolume(), avg);

            if(d.compareTo(disparity) >= 0){
                list.add(symbolCandle);
            }

        }

        if(validSymbolCount == 0){
            return data;
        }

        data.length = validSymbolCount;
        data.soaringArray = list.toArray(new SymbolCandle[0]);
        data.index = new BigDecimal(list.size()).divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP).stripTrailingZeros();
        return data;
    }


    public SoaringTradingVolumeData [] getArray(int resultLength){
        if(times == null){
            setTimes();
        }

        return getArray(times.length - resultLength, times.length);
    }

    public SoaringTradingVolumeData [] getArray(int startIndex, int end){
        if(times == null){
            setTimes();
        }

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > times.length){
            end = times.length;
        }

        if(startIndex >= end){
            //옵션을 잘 못 보
            throw new IllegalArgumentException("startIndex >= end  startIndex: " + startIndex +", end: " + end );
        }

        SoaringTradingVolumeData [] array = new SoaringTradingVolumeData[end - startIndex];
        for (int i = 0; i < array.length ; i++) {
            array[i] = getData(i + startIndex);
        }

        return  array;
    }

    public SymbolCandle[] getSymbolCandles() {
        return symbolCandles;
    }
}
