package io.runon.trading.technical.analysis;

import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.Data;

/**
 * 종목 구분 기화와 캔들 구현체
 * @author macle
 */
@Data
public class SymbolCandleData implements SymbolCandle {
    private String symbol;
    private TradeCandle [] candles;

    public SymbolCandleData(){

    }

    public SymbolCandleData(String symbol, TradeCandle [] candles){
        this.symbol = symbol;
        this.candles = candles;
    }



}
