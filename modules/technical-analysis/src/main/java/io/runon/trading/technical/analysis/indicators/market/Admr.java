package io.runon.trading.technical.analysis.indicators.market;

import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandle;
import io.runon.trading.technical.analysis.symbol.SymbolCandleTimes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 상승 하락 종목의 시장 비율
 * (A - D)/시장 종목의수
 *
 * D의 건수가 너무낮을 때 A가 커보이는 현상을 제외시키기 위해 개발하였다.
 *
 * -100 ~ 100
 * @author macle
 */
public class Admr extends MarketIndicators<TimeNumber> {


    public Admr(SymbolCandle[] symbolCandles) {
        super(symbolCandles);
    }

    public Admr(SymbolCandleTimes symbolCandleTimes) {
        super(symbolCandleTimes);

    }

    private BigDecimal minChangeRate = null;

    /**
     * 0 ~ 1.0
     * @param minChangeRate 0 ~ 1.0
     */
    public void setMinChangeRate(BigDecimal minChangeRate) {
        if(minChangeRate == null){
            this.minChangeRate = null;
            return;
        }

        if(minChangeRate.compareTo(BigDecimal.ZERO) < 0 || minChangeRate.compareTo(BigDecimal.ONE) > 0){
            throw new IllegalArgumentException("valid 0 ~ 1: " + minChangeRate.toPlainString());
        }


        this.minChangeRate = minChangeRate;
    }

    @Override
    public TimeNumber getData(int index) {
        TimeNumberData data = new TimeNumberData();
        long time = times[index];

        data.setTime(time);
        int searchLength = searchIndex(index);

        int advancing = 0;
        int decline = 0;
        int marketCount = 0;

        for(SymbolCandle symbolCandle : symbolCandles){
            TradeCandle[] candles = symbolCandle.getCandles();
            int openTimeIndex = TaCandles.getOpenTimeIndex(candles, time, searchLength);
            if(openTimeIndex == -1){
                continue;
            }

            TradeCandle candle = candles[openTimeIndex];
            if(minTradingPrice != null &&  candle.getTradingPrice().compareTo(minTradingPrice) < 0) {
                continue;
            }

            BigDecimal change = candle.getChange();
            if(change == null){
                change =candle.getChangeRate();
            }

            if(change == null){
                continue;
            }

            marketCount++;

            int compare = change.compareTo(BigDecimal.ZERO);

            if(compare == 0){
                continue;
            }

            if(minChangeRate != null){
                BigDecimal changeRate = candle.getChangeRate();
                if(changeRate == null){
                    continue;
                }
                BigDecimal abs = changeRate.abs();

            }

            if(compare > 0){
                advancing ++;
            }else{
                decline ++;
            }
        }

        data.setNumber(getAdmr(advancing, decline, marketCount).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros());
        return data;

    }

    public static BigDecimal getAdmr(int advancing, int decline, int marketCount){
        if(marketCount == 0){
            return BigDecimal.ZERO;
        }

        return new BigDecimal(advancing - decline).multiply(BigDecimals.DECIMAL_100).divide(new BigDecimal(marketCount), MathContext.DECIMAL128);
    }

    @Override
    public TimeNumber[] newArray(int startIndex, int end) {
        TimeNumber[] array = new TimeNumber[end - startIndex];

        for (int i = 0; i < array.length ; i++) {
            array[i] = getData(i + startIndex);
        }
        return array;
    }
}
