package io.runon.trading.technical.analysis.candle;

/**
 * 종목 구분 기화와 캔들
 * @author macle
 */
public interface IdCandlesGet extends CandlesGet {
    IdCandlesGet[] EMPTY_ARRAY = new IdCandlesGet[0];

    String getId();

    static IdCandlesGet getIdCandle(String id, IdCandlesGet[] idCandles){
        for(IdCandlesGet symbolCandle : idCandles){
            if(symbolCandle.getId().equals(id)){
                return symbolCandle;
            }
        }

        return null;
    }

    static TradeCandle [] getCandles(String id, IdCandlesGet[] idCandles){
        for(IdCandlesGet symbolCandle : idCandles){
            if(symbolCandle.getId().equals(id)){
                return symbolCandle.getCandles();
            }
        }

        return null;
    }

}
