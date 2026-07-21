package io.runon.trading.technical.analysis.indicators.market.mv;

import io.runon.commons.math.BigDecimalMath;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.IdCandleTimes;
import io.runon.trading.technical.analysis.candle.IdCandlesGet;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.Disparity;
import io.runon.trading.technical.analysis.volume.Volumes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * Market Amount Disparity
 * 0 ~ 1000
 * market.volume.disparity.max 설정값에 최대값은 변할 수 있음
 * 거래대금으로 보는 이격도
 * @author macle
 */
public class Mad extends Mvd{
    public Mad(IdCandleTimes symbolCandleTimes){
        super(symbolCandleTimes);
    }

    public Mad(IdCandlesGet[] idCandles) {
        super(idCandles);
    }


    @Override
    public TimeNumber getData(int index) {

        //1. 개별종목의 평균거래대금 구하기
        //2. 개별종목의 평균 거래대금의 합

        //3. 타겟날짜의 종목 거래대금의 합.


        int minCount = minAverageCount + 1;
        if(minCount > averageCount + 1) {
            minCount = averageCount + 1;
        }

        long time = times[index];

        int length = index + 1;
        if( length < minCount){
            return new TimeNumberData(time, BigDecimal.ZERO);
        }
        int avgStartIndex = times.length - index- averageCount -1;
        if(avgStartIndex < 0){
            avgStartIndex = 0;
        }

        long avgStartTime = times[avgStartIndex];

        int validSymbolCount = 0;

        int searchLength = searchIndex(index);


        BigDecimal sum = BigDecimal.ZERO;

        for(IdCandlesGet idCandle : idCandles){
            TradeCandle[] candles = idCandle.getCandles();
            if(candles.length < minCount){
                continue;
            }

            int openTimeIndex = Candles.getOpenTimeIndex(candles, time, searchLength);
            if(openTimeIndex == -1){

                continue;
            }

            if(openTimeIndex +1 < minCount){

                continue;
            }

            int averageStartIndex =  Candles.getNearOpenTimeIndex(candles, avgStartTime, searchLength + (index - avgStartIndex));
            if (averageStartIndex == -1){

                continue;
            }

            if(averageStartIndex >= openTimeIndex){

                continue;
            }

            if(openTimeIndex - averageStartIndex +1 < minCount){
                continue;
            }

            TradeCandle candle = candles[openTimeIndex];
            if(minAmount != null &&  candle.getAmount().compareTo(minAmount) < 0) {
                continue;
            }

            BigDecimal [] amounts = Volumes.getAmounts(candles, averageStartIndex , openTimeIndex);
            Arrays.sort(amounts);
            BigDecimal avg = BigDecimalMath.average(amounts, exclusionRate, exclusionRate);
            if(avg.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }

            validSymbolCount++;


            TradeCandle tradeCandle = candles[openTimeIndex];
            BigDecimal disparity = Disparity.get(tradeCandle.getAmount(), avg);

            if(disparity.compareTo(maxDisparity) > 0){
                disparity = maxDisparity;
            }
            sum = sum.add(disparity);
        }

        if(validSymbolCount == 0 || sum.compareTo(BigDecimal.ZERO) == 0){
//            return new TimeNumberData(time, BigDecimal.ZERO);
            return null;

        }

        if(scale > 0){
            return new TimeNumberData(time, sum.divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP).stripTrailingZeros());
        }else{
            return new TimeNumberData(time, sum.divide(new BigDecimal(validSymbolCount), MathContext.DECIMAL128).stripTrailingZeros());
        }


    }
}
