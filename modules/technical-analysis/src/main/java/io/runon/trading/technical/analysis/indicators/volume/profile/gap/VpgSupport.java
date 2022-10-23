package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import com.seomse.commons.data.BeginEnd;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;
import io.runon.trading.technical.analysis.hl.HighLowN;

/**
 * 지지 매물대 분석
 * @author macle
 */
public class VpgSupport  extends HighLowN {

    private final Vpg vpg;

    public VpgSupport(Vpg vpg){
        this.vpg = vpg;
    }


    public VpgData [] getArray(TradeCandle[] longCandles, int longIndex, TradeCandle [] shortCandles, long shortCandleTime ){

        if(shortCandles.length ==0 || longCandles.length == 0){
            return VpgData.EMPTY_ARRAY;
        }

        HighLow highLow = HighLowCandleLeftSearch.getLowNextHigh(longCandles, initN, continueN, longIndex);

        BeginEnd beginEnd = TaCandles.getNearTimeRange(shortCandles, shortCandleTime, longCandles[highLow.getLowIndex()].getOpenTime(), longCandles[highLow.getHighIndex()].getCloseTime());

        if(beginEnd.getBegin() < 0){
            return VpgData.EMPTY_ARRAY;
        }

        return vpg.getArray(shortCandles, beginEnd.getBegin(), beginEnd.getEnd());
    }

}
