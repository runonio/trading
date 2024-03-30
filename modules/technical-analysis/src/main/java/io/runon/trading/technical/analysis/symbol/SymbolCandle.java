package io.runon.trading.technical.analysis.symbol;

import io.runon.trading.technical.analysis.candle.GetCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * 종목 구분 기화와 캔들
 * @author macle
 */
public interface SymbolCandle extends GetCandles {
    SymbolCandle[] EMPTY_ARRAY = new SymbolCandle[0];

    String getSymbol();

    static SymbolCandle getSymbolCandle(String symbol, SymbolCandle [] symbolCandles){
        for(SymbolCandle symbolCandle : symbolCandles){
            if(symbolCandle.getSymbol().equals(symbol)){
                return symbolCandle;
            }
        }

        return null;
    }

    static TradeCandle [] getCandles(String symbol, SymbolCandle [] symbolCandles){
        for(SymbolCandle symbolCandle : symbolCandles){
            if(symbolCandle.getSymbol().equals(symbol)){
                return symbolCandle.getCandles();
            }
        }

        return null;
    }

}
