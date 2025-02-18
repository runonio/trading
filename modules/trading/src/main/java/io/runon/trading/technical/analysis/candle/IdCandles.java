package io.runon.trading.technical.analysis.candle;

/**
 * 종목 구분 기화와 캔들
 * @author macle
 */
public interface IdCandles extends GetCandles {
    IdCandles[] EMPTY_ARRAY = new IdCandles[0];

    String getId();

    static IdCandles getIdCandle(String id, IdCandles[] idCandles){
        for(IdCandles symbolCandle : idCandles){
            if(symbolCandle.getId().equals(id)){
                return symbolCandle;
            }
        }

        return null;
    }

    static TradeCandle [] getCandles(String id, IdCandles[] idCandles){
        for(IdCandles symbolCandle : idCandles){
            if(symbolCandle.getId().equals(id)){
                return symbolCandle.getCandles();
            }
        }

        return null;
    }

}
