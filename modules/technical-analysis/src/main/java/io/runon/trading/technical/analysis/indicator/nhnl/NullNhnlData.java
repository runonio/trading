package io.runon.trading.technical.analysis.indicator.nhnl;

import io.runon.trading.technical.analysis.SymbolCandle;

import java.math.BigDecimal;

/**
 * null데이터 변경 불가능
 * @author macle
 */
class NullNhnlData extends NhnlData{

    NullNhnlData(){
        validSymbolCount = 0;
        index = BigDecimal.ZERO;
        highs = SymbolCandle.EMPTY_ARRAY;
        lows = SymbolCandle.EMPTY_ARRAY;
    }

    @Override
    public void setValidSymbolCount(int validSymbolCount) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void setIndex(BigDecimal index) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void setHighs(SymbolCandle[] highSymbolCandles) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void setLows(SymbolCandle[] lowSymbolCandles) {
        throw new UnsupportedOperationException();
    }
}
