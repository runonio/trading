package io.runon.trading.technical.analysis.indicators.market;

import io.runon.commons.math.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.IdCandleTimes;
import io.runon.trading.technical.analysis.candle.IdCandlesGet;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.ma.Sma;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 시장 생존 비율
 * 이평선과 겹치거나 위에 있는 종목수의 비율
 * @author macle
 */
public class MarketSurvivalRate extends MarketIndicatorsTimeNumber {


    public MarketSurvivalRate(IdCandlesGet[] idCandles) {
        super(idCandles);
    }

    public MarketSurvivalRate(IdCandleTimes idCandleTimes) {
        super(idCandleTimes);

    }

    private int defaultN = 200;

    public void setDefaultN(int defaultN) {
        this.defaultN = defaultN;
    }

    @Override
    public TimeNumber getData(int index) {
        TimeNumberData data = new TimeNumberData();
        long time = times[index];
        data.setTime(time);
        int searchLength = searchIndex(index);

        int validSymbolCount = 0;
        int survivalCount = 0;
        for(IdCandlesGet symbolCandle : idCandles){
            TradeCandle[] candles = symbolCandle.getCandles();
            int openTimeIndex = Candles.getOpenTimeIndex(candles, time, searchLength);
            if(openTimeIndex == -1){
                continue;
            }

            TradeCandle candle = candles[openTimeIndex];
            if(minAmount != null &&  candle.getAmount().compareTo(minAmount) < 0) {
                continue;
            }

            if(openTimeIndex+1 < defaultN){
                continue;
            }
            validSymbolCount++;

            BigDecimal ma = Sma.get(candles, defaultN, openTimeIndex);

            if(candle.getClose().compareTo(ma) >= 0){
                survivalCount++;
            }
        }

        if(validSymbolCount == 0){
//            data.setNumber(BigDecimal.ZERO);
//            return data;

            return null;
        }

        if(scale> 0){
            data.setNumber(new BigDecimal(survivalCount).multiply(BigDecimals.DECIMAL_100).divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP).stripTrailingZeros());
        }else{
            data.setNumber(new BigDecimal(survivalCount).multiply(BigDecimals.DECIMAL_100).divide(new BigDecimal(validSymbolCount), MathContext.DECIMAL128).stripTrailingZeros());
        }

        return data;

    }



}
