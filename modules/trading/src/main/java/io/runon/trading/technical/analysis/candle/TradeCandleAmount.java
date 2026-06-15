package io.runon.trading.technical.analysis.candle;


import io.runon.commons.data.GetNumber;

import java.math.BigDecimal;
import java.util.List;

/**
 *거래 분석에 사용할 수 있는 캔들
 * 기본정보외에 분석에 필요한 거래정보 추가
 * @author macle
 */
public class TradeCandleAmount implements GetNumber {


    private final TradeCandle tradeCandle;

    public TradeCandleAmount(TradeCandle tradeCandle){
        this.tradeCandle = tradeCandle;
    }


    @Override
    public BigDecimal getNumber() {
        return tradeCandle.getAmount();
    }


    public TradeCandle getCandle() {
        return tradeCandle;
    }

    public static TradeCandleAmount [] newArray(List<TradeCandle> candleList){
        TradeCandleAmount [] array = new TradeCandleAmount[candleList.size()];

        for (int i = 0; i < candleList.size(); i++) {
            array[i] = new TradeCandleAmount(candleList.get(i));
        }

        return array;
    }

    public static TradeCandleAmount [] newArray(TradeCandle []  candles){
        TradeCandleAmount [] array = new TradeCandleAmount[candles.length];

        for (int i = 0; i < candles.length; i++) {
            array[i] = new TradeCandleAmount(candles[i]);
        }

        return array;
    }



}
