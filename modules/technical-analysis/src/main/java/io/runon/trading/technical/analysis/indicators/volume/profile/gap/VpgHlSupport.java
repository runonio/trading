package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;

import java.math.BigDecimal;

/**
 * 고가 저가를 활용한
 * 지지 매물대 분석
 * @author macle
 */
public class VpgHlSupport extends VpgHl {


    public VpgHlSupport(Vpg vpg){
        super(vpg);

    }

    @Override
    public VpgDataTimeRange getRange(TradeCandle[] candles, long time) {
        int index = TaCandles.getNearOpenTimeIndex(candles, candleTime, time);
        TradeCandle candle = candles[index];
        HighLow highLow = HighLowCandleLeftSearch.getLowNextHigh(candles, initN, continueN, index, maxNextInit, candle.getHigh());

        VpgDataTimeRange range = new VpgDataTimeRange();
        range.openTime = candles[highLow.getLowIndex()].getOpenTime();
        range.closeTime = candles[highLow.getHighIndex()].getCloseTime();
        range.startIndex = highLow.getLowIndex();
        range.end = highLow.getHighIndex()+1;
        return range;

    }

    @Override
    public void setFibonacci() {

    }

    @Override
    public BigDecimal getReverseVolume(TradeCandle[] candles, long time) {

        VpgData [] dataArray = getDataArray(candles, time);

        int index = TaCandles.getNearOpenTimeIndex(candles, candleTime, time);
        int end = index + 1;

        BigDecimal sum = BigDecimal.ZERO;

        TradeCandle candle = candles[index];
        BigDecimal close = candle.getClose();

        int start = TaCandles.getNearCloseTimeIndex(candles, candleTime,  lastRange.closeTime);
        if(candles[start].getCloseTime() == lastRange.closeTime){
            start++;
        }

        for (int i = start; i < end; i++) {
            TradeCandle compare = candles[i];
            //고가가 저가보다 작으면 누적
            if(compare.getLow().compareTo(close) > 0 ){
                sum = sum.add(compare.getVolume());
            }
        }

        for (int i = dataArray.length - 1; i > -1 ; i--) {
            VpgData data = dataArray[i];
            if(data.getPrice().compareTo(close) <= 0){
                break;
            }
            sum = sum.add(data.getVolume());
        }

        return sum;
    }


}
