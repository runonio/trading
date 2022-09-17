package io.runon.trading.technical.analysis.indicators.market.mvd;

import com.seomse.commons.config.Config;
import io.runon.trading.technical.analysis.indicators.market.MarketIndicator;
import io.runon.trading.technical.analysis.indicators.market.stv.SoaringTradingVolumeData;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandleTimes;

import java.math.BigDecimal;

/**
 * Market Volume Disparity
 * @author macle
 */
public class Mvd  extends MarketIndicator<SoaringTradingVolumeData> {

    private int averageCount = 50;

    private int minAverageCount = 10;

    private BigDecimal highestExclusionRate = new BigDecimal(Config.getConfig("soaring.trading.volume.default.highest.exclusion.rate", "0.1"));


    public void setAverageCount(int averageCount) {
        this.averageCount = averageCount;
    }

    public void setMinAverageCount(int minAverageCount) {
        this.minAverageCount = minAverageCount;
    }

    public void setHighestExclusionRate(BigDecimal highestExclusionRate) {
        this.highestExclusionRate = highestExclusionRate;
    }
    
    public Mvd(SymbolCandle[] symbolCandles){
        super(symbolCandles);
    }
    public Mvd(SymbolCandleTimes symbolCandleTimes){
        super(symbolCandleTimes);
    }

    @Override
    public SoaringTradingVolumeData getData(int index) {
        return null;
    }

    @Override
    public SoaringTradingVolumeData[] newArray(int startIndex, int end) {
        return new SoaringTradingVolumeData[0];
    }
}
