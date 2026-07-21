package io.runon.trading.technical.analysis.candle;

import io.runon.commons.math.BigDecimals;
import io.runon.trading.Candle;

import java.math.BigDecimal;

/**
 *
 * @author macle
 */
public class PerformanceCandleGenerator {


    protected BigDecimal previous = BigDecimals.DECIMAL_1000;

    public PerformanceCandleGenerator(BigDecimal previous){
        this.previous = previous;
    }

    public PerformanceCandleGenerator(){

    }

    public TradeCandle [] nextCandles(Candle[] candles){

        TradeCandle [] tradeCandles = new TradeCandle[candles.length];
        for (int i = 0; i < tradeCandles.length ; i++) {
            tradeCandles[i] = nextCandle(candles[i]);
        }

        return tradeCandles;
    }



    public TradeCandle nextCandle(Candle candle){


        BigDecimal low = previous.add(previous.multiply(candle.getLow()));
        BigDecimal high = previous.add(previous.multiply(candle.getHigh()));
        BigDecimal open= previous.add(previous.multiply(candle.getOpen()));
        BigDecimal close= previous.add(previous.multiply(candle.getClose()));

        TradeCandle tradeCandle = new TradeCandle();
        tradeCandle.setOpenTime(candle.getTime());
        tradeCandle.setLow(low);
        tradeCandle.setHigh(high);
        tradeCandle.setOpen(open);
        tradeCandle.setClose(close);

        tradeCandle.setPrevious(previous);

        tradeCandle.setChangeRate(candle.getClose());

        return tradeCandle;
    }


}
