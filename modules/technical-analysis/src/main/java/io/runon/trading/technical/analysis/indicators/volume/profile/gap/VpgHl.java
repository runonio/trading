package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.hl.HighLowN;
import io.runon.trading.technical.analysis.volume.Volumes;

import java.math.BigDecimal;

/**
 * @author macle
 */
public abstract class VpgHl extends HighLowN {

    protected final Vpg vpg;
    public VpgHl(Vpg vpg){
        this.vpg = vpg;
    }


    //최대 다음 매물대 분석 //1분봉 기준 52만개 이므로 최대 5300개까지 반복
    protected int maxNextInit = 5300;

    public void setMaxNextInit(int maxNextInit) {
        this.maxNextInit = maxNextInit;
    }

    protected VpgDataTimeRange lastRange = null;
    protected VpgData [] dataArray = null;

    protected BigDecimal volumeSum = BigDecimal.ZERO;

    public abstract VpgDataTimeRange getRange(TradeCandle [] candles, long time);

    public VpgData [] getDataArray(TradeCandle [] candles, long time){
        setData(candles, time);
        return dataArray;
    }

    public BigDecimal getHigh(){
        if(dataArray == null || dataArray.length == 0){
            return null;
        }

        return dataArray[dataArray.length -1].getPrice();
    }

    public void setData(TradeCandle [] candles, long time){
        VpgDataTimeRange range = getRange(candles, time);

        if(lastRange != null && lastRange.openTime == range.openTime && lastRange.closeTime == range.closeTime){
            //이전 측정치와 같으면 다시 만들지 않음
            return ;
        }

        lastRange = range;
        dataArray = vpg.getArray(candles, range.startIndex, range.end);
        volumeSum = Volumes.sum(candles, range.startIndex, range.end);

        setFibonacci();
    }


    public BigDecimal getLow(){
        if(dataArray == null || dataArray.length == 0){
            return null;
        }

        return dataArray[0].getPrice();
    }

    public abstract void setFibonacci();


    public abstract BigDecimal getReverseVolume(TradeCandle [] candles, long time);

}