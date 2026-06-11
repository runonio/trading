package io.runon.trading.technical.analysis.indicators.market.nhnl;

import io.runon.commons.utils.time.Times;
import io.runon.commons.math.BigDecimals;
import io.runon.trading.exception.CandleTimeException;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.IdCandlesGet;
import io.runon.trading.technical.analysis.candle.IdCandleTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.market.MarketIndicators;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 신고가 신저가 지수
 * New High New Low
 * - 100 ~ 100
 *
 * 분산통계 작업
 *
 * @author macle
 */
public class Nhnl extends MarketIndicators<NhnlData> {

    /**
     * 캔들 정보를 활용한 분석
     */
    public Nhnl(IdCandlesGet[] idCandles){
        super(idCandles);
    }

    public Nhnl(IdCandleTimes idCandleTimes) {
        super(idCandleTimes);
    }

    private long candleTime = -1;
    private long timeRange = Times.WEEK_52;


    public void setCandleTime(long candleTime) {
        this.candleTime = candleTime;
    }


    public void setTimeRange(long timeRange) {
        this.timeRange = timeRange;
    }

    @Override
    public NhnlData getData(int index){

        if(candleTime < 1){
            throw new CandleTimeException("candle time error: " + candleTime);
        }

        long candleOpenTime = times[index];

        NhnlData data = new NhnlData();
        data.time = candleOpenTime;

        int validSymbolCount = 0;

        List<IdCandlesGet> highList = new ArrayList<>();
        List<IdCandlesGet> lowList = new ArrayList<>();

        long rangeEndTime = candleOpenTime - timeRange + candleTime;
        int searchLength = searchIndex(index);

        for(IdCandlesGet symbolCandle : idCandles){
            TradeCandle [] candles = symbolCandle.getCandles();

            int openTimeIndex = Candles.getOpenTimeIndex(candles, data.time, searchLength);


            if(openTimeIndex < 1 ){
                continue;
            }

            //적어도 searchTime 의 2배 이상은 되어야함함

            TradeCandle candle = candles[openTimeIndex];

            if(minAmount != null &&  candle.getAmount().compareTo(minAmount) < 0) {
                continue;
            }

            int rangeEndIndex = openTimeIndex-1;

            BigDecimal rangeHigh = candles[rangeEndIndex].getHigh();
            BigDecimal rangeLow = candles[rangeEndIndex].getLow();
            for (int i = rangeEndIndex - 1; i > -1; i--) {
                TradeCandle rangeCandle = candles[i];
                if(rangeCandle.getCloseTime() < rangeEndTime || rangeCandle.getOpenTime() < rangeEndTime){
                    break;
                }
                if (rangeCandle.getHigh().compareTo(rangeHigh) > 0){
                    rangeHigh = candle.getHigh();
                }

                if(rangeCandle.getLow().compareTo(rangeLow) < 0){
                    rangeLow = candle.getLow();
                }
            }

            if(candle.getHigh().compareTo(rangeHigh) > 0){
                highList.add(symbolCandle);
            }

            if(candle.getLow().compareTo(rangeLow) < 0){
                lowList.add(symbolCandle);
            }

            validSymbolCount++;
        }

        if(validSymbolCount == 0){
            return data;
        }

        data.length = validSymbolCount;

        if(!highList.isEmpty()){
            data.highs = highList.toArray(new IdCandlesGet[0]);
            highList.clear();
        }

        if(!lowList.isEmpty()){
            data.lows = lowList.toArray(new IdCandlesGet[0]);
            lowList.clear();
        }

        data.ratio = new BigDecimal(data.highs.length - data.lows.length).multiply(BigDecimals.DECIMAL_100).divide(new BigDecimal(validSymbolCount), scale, RoundingMode.HALF_UP).stripTrailingZeros();
        return data;
    }

    @Override
    protected NhnlData[] newEmptyArray(int length) {
        return new NhnlData[length];
    }

}
