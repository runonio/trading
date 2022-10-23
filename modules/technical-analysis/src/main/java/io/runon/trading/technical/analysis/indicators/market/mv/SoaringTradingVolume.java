package io.runon.trading.technical.analysis.indicators.market.mv;

import com.seomse.commons.config.Config;
import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.Disparity;
import io.runon.trading.technical.analysis.indicators.market.MarketIndicators;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandleTimes;
import io.runon.trading.technical.analysis.volume.Volumes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  soaring trading volume : 거래량 급증 종목수/ 전체 종목수  (아이디어로 새로 만들어보고 시험해보고자 하는 지표)
 * 거래량이 급등하는 종목의 수를 지수화 함
 * 0 ~ 100
 * @author macle
 */
public class SoaringTradingVolume extends MarketIndicators<SoaringTradingVolumeData> {


    private int averageCount =  Config.getInteger("volume.average.count", 50);

    private int minAverageCount = Config.getInteger("volume.average.min.count", 10);

    private BigDecimal highestExclusionRate = new BigDecimal(Config.getConfig("volume.average.default.highest.exclusion.rate", "0.1"));

    private BigDecimal disparity = new BigDecimal(Config.getConfig("soaring.trading.volume.disparity", "300"));

    public SoaringTradingVolume(SymbolCandle[] symbolCandles){
        super(symbolCandles);
    }
    public SoaringTradingVolume(SymbolCandleTimes symbolCandleTimes){
        super(symbolCandleTimes);
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

    @Override
    public SoaringTradingVolumeData getData(int index){
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

        int avgStartIndex = times.length - index- averageCount -1;
        if(avgStartIndex < 0){
            avgStartIndex = 0;
        }

        long avgStartTime = times[avgStartIndex];

        int validSymbolCount = 0;

        List<SymbolCandle> list = new ArrayList<>();
        List<SymbolCandle> upList = new ArrayList<>();
        List<SymbolCandle> downList = new ArrayList<>();
        int searchLength = searchIndex(index);
        int check = (times.length - index);

        for(SymbolCandle symbolCandle : symbolCandles){
            TradeCandle[] candles = symbolCandle.getCandles();
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
            BigDecimal d = Disparity.get(tradeCandle.getVolume(), avg);



            if(d.compareTo(disparity) >= 0){
                list.add(symbolCandle);

                if(tradeCandle.getChange().compareTo(BigDecimal.ZERO) > 0){
                    upList.add(symbolCandle);
                }else if(tradeCandle.getChange().compareTo(BigDecimal.ZERO) < 0){
                    downList.add(symbolCandle);
                }
            }
        }

        if(validSymbolCount == 0){
            return data;
        }

        data.length = validSymbolCount;
        data.soaringArray = list.toArray(new SymbolCandle[0]);
        data.ups = upList.toArray(new SymbolCandle[0]);
        data.downs = downList.toArray(new SymbolCandle[0]);
        data.ratio = new BigDecimal(list.size()).multiply(BigDecimals.DECIMAL_100).divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP).stripTrailingZeros();
        return data;
    }

    @Override
    public SoaringTradingVolumeData[] newArray(int startIndex, int end) {
        SoaringTradingVolumeData[] array = new SoaringTradingVolumeData[end - startIndex];
        for (int i = 0; i < array.length ; i++) {
            array[i] = getData(i + startIndex);
        }
        return array;
    }

}
