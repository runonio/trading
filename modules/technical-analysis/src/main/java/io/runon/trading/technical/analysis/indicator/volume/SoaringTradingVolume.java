package io.runon.trading.technical.analysis.indicator.volume;

import com.seomse.commons.config.Config;
import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.SymbolCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;
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

    public static final SoaringTradingVolumeNullData NULL_DATA = new SoaringTradingVolumeNullData();
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
        this.symbolCandles = symbolCandles;
    }

    public void setSymbolCandles(SymbolCandle[] symbolCandles) {
        this.symbolCandles = symbolCandles;
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
        int minCount = minAverageCount + 1;
        if(minCount > averageCount + 1){
            minCount = averageCount + 1 ;
        }

        int validSymbolCount = 0;

        List<SymbolCandle> list = new ArrayList<>();

        for(SymbolCandle symbolCandle : symbolCandles){
            TradeCandle[] candles =symbolCandle.getCandles();
            if(candles.length < minCount){
                continue;
            }

            BigDecimal [] volumes = Volumes.getVolumes(candles, candles.length - averageCount-1 , candles.length-1);
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
            return NULL_DATA;
        }

        SoaringTradingVolumeData data = new SoaringTradingVolumeData();
        data.validSymbolCount = validSymbolCount;
        data.soaringArray = list.toArray(new SymbolCandle[0]);
        data.index = new BigDecimal(list.size()).divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP).stripTrailingZeros();
        return data;
    }

    public SoaringTradingVolumeData [] getArray(int dataCount){
        int minCount = minAverageCount + 1;
        if(minCount > averageCount + 1){
            minCount = averageCount + 1 ;
        }

        SoaringTradingVolumeData [] array = new SoaringTradingVolumeData[dataCount];

        for (int i = 0; i <dataCount ; i++) {
            int validSymbolCount = 0;
            List<SymbolCandle> list = new ArrayList<>();

            int gap = dataCount - (i +1);

            for(SymbolCandle symbolCandle : symbolCandles){
                TradeCandle[] candles =symbolCandle.getCandles();
                int end = candles.length - gap;
                if(end < minCount){
                    continue;
                }
                BigDecimal [] volumes = Volumes.getVolumes(candles, end - averageCount - 1 , end - 1);
                Arrays.sort(volumes);
                BigDecimal avg = BigDecimals.average(volumes, highestExclusionRate);
                if(avg.compareTo(BigDecimal.ZERO) == 0){
                    continue;
                }
                validSymbolCount++;
                BigDecimal d = Disparity.get(candles[end-1].getVolume(), avg);

                if(d.compareTo(disparity) >= 0){
                    list.add(symbolCandle);
                }
            }

            if(validSymbolCount == 0){
                array[i] = NULL_DATA;
                continue;
            }
            SoaringTradingVolumeData data = new SoaringTradingVolumeData();
            data.validSymbolCount = validSymbolCount;
            data.soaringArray = list.toArray(new SymbolCandle[0]);
            data.index = new BigDecimal(list.size()).divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP).stripTrailingZeros();
            array[i] = data;
        }

        return  array;

    }

    public SymbolCandle[] getSymbolCandles() {
        return symbolCandles;
    }
}
