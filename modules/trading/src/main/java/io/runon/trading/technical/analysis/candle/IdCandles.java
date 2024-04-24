package io.runon.trading.technical.analysis.candle;

/**
 * 종목 구분 기화와 캔들
 * @author macle
 */
public interface IdCandles extends GetCandles {
    IdCandles[] EMPTY_ARRAY = new IdCandles[0];

    String getId();

    static IdCandles getSymbolCandle(String symbol, IdCandles[] symbolCandles){
        for(IdCandles symbolCandle : symbolCandles){
            if(symbolCandle.getId().equals(symbol)){
                return symbolCandle;
            }
        }

        return null;
    }

    static TradeCandle [] getCandles(String symbol, IdCandles[] symbolCandles){
        for(IdCandles symbolCandle : symbolCandles){
            if(symbolCandle.getId().equals(symbol)){
                return symbolCandle.getCandles();
            }
        }

        return null;
    }

}
