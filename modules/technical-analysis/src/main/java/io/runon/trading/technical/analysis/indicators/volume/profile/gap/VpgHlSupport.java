package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;

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


}
