package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import com.seomse.commons.data.BeginEnd;
import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;
import io.runon.trading.technical.analysis.hl.HighLowN;

/**
 * 고가 저가를 활용한
 * 저항 매물대 분석
 * @author macle
 */
public class VpgHlResistance extends HighLowN {

    private final Vpg vpg;

    public VpgHlResistance(Vpg vpg){
        this.vpg = vpg;
    }

    /**
     * 긴 시간대로 범위를 구하고 짧은 시간대로 매물대 정보를 구한다
     */
    public VpgDataTimeRange getData(TradeCandle [] longCandles, int longIndex, TradeCandle [] shortCandles, long shortCandleTime ){

        if(shortCandles.length ==0 || longCandles.length == 0){
            return null;
        }

        HighLow highLow = HighLowCandleLeftSearch.getHighNextLow(longCandles, initN, continueN, longIndex);

        long openTime = longCandles[highLow.getHighIndex()].getOpenTime();
        long closeTime = longCandles[highLow.getLowIndex()].getCloseTime();


        TradeCandle longHighCandle = longCandles[highLow.getHighIndex()];
        BeginEnd highBeginEnd =  TaCandles.getNearTimeRange(shortCandles, shortCandleTime, longHighCandle.getOpenTime(), longHighCandle.getCloseTime());
        if(highBeginEnd.getBegin() < 0){
            return null;
        }
        int highIndex = HighLowCandleLeftSearch.searchHigh(shortCandles, highBeginEnd.getBegin(), highBeginEnd.getEnd());

        TradeCandle longLowCandle = longCandles[highLow.getLowIndex()];
        BeginEnd lowBeginEnd =  TaCandles.getNearTimeRange(shortCandles, shortCandleTime, longLowCandle.getOpenTime(), longLowCandle.getCloseTime());
        if(lowBeginEnd.getBegin() < 0){
            return null;
        }
        int lowIndex = HighLowCandleLeftSearch.searchLow(shortCandles, lowBeginEnd.getBegin(), lowBeginEnd.getEnd());

        VpgData [] dataArray = vpg.getArray(shortCandles, highIndex, lowIndex+1);

        if(dataArray.length == 0){
            return null;
        }

        TradeCandle longCandle = longCandles[longIndex];

        VpgDataTimeRange data = new VpgDataTimeRange();
        data.longTime = longCandle.getTime();
        data.openTime = shortCandles[highIndex].getOpenTime();
        data.closeTime = shortCandles[lowIndex].getCloseTime();

        data.high =  shortCandles[highIndex].getHigh();
        data.low = shortCandles[lowIndex].getLow();

        data.dataArray = dataArray;
        return data;
    }


}
