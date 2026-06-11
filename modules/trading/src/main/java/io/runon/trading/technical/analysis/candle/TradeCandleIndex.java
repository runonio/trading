package io.runon.trading.technical.analysis.candle;


import lombok.Data;
/**
 * @author macle
 */
@Data
public class TradeCandleIndex {

    TradeCandle tradeCandle = null;
    int index = -1;

    public TradeCandleIndex(){

    }

    public TradeCandleIndex(TradeCandle tradeCandle, int index){
        this.tradeCandle = tradeCandle;
        this.index = index;
    }

}
