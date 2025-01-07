package io.runon.trading.technical.analysis.candle;


import lombok.Data;
/**
 * @author macle
 */
@Data
public class TradeCandleIndex {

    TradeCandle tradeCandle;
    int index;

    public TradeCandleIndex(){

    }

    public TradeCandleIndex(TradeCandle tradeCandle, int index){
        this.tradeCandle = tradeCandle;
        this.index = index;
    }

}
