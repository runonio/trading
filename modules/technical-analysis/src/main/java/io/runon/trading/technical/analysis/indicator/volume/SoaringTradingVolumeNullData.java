package io.runon.trading.technical.analysis.indicator.volume;

import io.runon.trading.technical.analysis.SymbolCandle;

import java.math.BigDecimal;

/**
 * @author macle
 */
class SoaringTradingVolumeNullData extends SoaringTradingVolumeData{

    SoaringTradingVolumeNullData(){
        soaringArray = SymbolCandle.EMPTY_ARRAY;
        index = BigDecimal.ZERO;
        validSymbolCount = 0;
    }

    public void setSoaringArray(SymbolCandle[] soaringArray) {
        throw new UnsupportedOperationException();
    }

    public void setIndex(BigDecimal index) {
        throw new UnsupportedOperationException();
    }

    public void setValidSymbolCount(int validSymbolCount) {
        throw new UnsupportedOperationException();
    }
}
