package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import com.seomse.commons.data.BeginEnd;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;
import io.runon.trading.technical.analysis.hl.HighLowN;

/**
 * 저항 매물대 분석
 * @author macle
 */
public class VpgResistance extends HighLowN {

    private final Vpg vpg;

    public VpgResistance(Vpg vpg){
        this.vpg = vpg;
    }

    /**
     * 긴 시간대로 범위를 구하고 짧은 시간대로 매물대 정보를 구한다
     *
     */
    public VpgData [] getArray(TradeCandle [] longCandles, int longIndex, TradeCandle [] shortCandles, long shortCandleTime ){

        if(shortCandles.length ==0 || longCandles.length == 0){
            return VpgData.EMPTY_ARRAY;
        }


        HighLow highLow = HighLowCandleLeftSearch.getHighNextLow(longCandles, initN, continueN, longIndex);

        BeginEnd beginEnd = TaCandles.getNearTimeRange(shortCandles, shortCandleTime, longCandles[highLow.getHighIndex()].getOpenTime(), longCandles[highLow.getLowIndex()].getCloseTime());

        if(beginEnd.getBegin() < 0){
            return VpgData.EMPTY_ARRAY;
        }

        return vpg.getArray(shortCandles, beginEnd.getBegin(),beginEnd.getEnd());
    }



}
