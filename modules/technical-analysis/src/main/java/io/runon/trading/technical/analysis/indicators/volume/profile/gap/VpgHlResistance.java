package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import io.runon.trading.technical.analysis.candle.TaCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.hl.HighLow;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;
import io.runon.trading.technical.analysis.hl.HighLowN;

import java.math.BigDecimal;

/**
 * 고가 저가를 활용한
 * 저항 매물대 분석
 * 상승 추세 분석
 *
 * 하락관점에서의 저항선 구간
 * 이 구간을 의미있게 돌파하면 추세전환을 예고한다
 * 여기서의 추세전환은 지금 분석된 매물대를 돌파하는 구간을 이야기한다.
 *
 * @author macle
 */
public class VpgHlResistance extends VpgHl {

    public VpgHlResistance(Vpg vpg){
        super(vpg);
    }

    public VpgDataTimeRange getRange(TradeCandle [] candles, long time){
        if(candles.length ==0 ){
            return null;
        }
        //구간분석
        
        int index = TaCandles.getNearOpenTimeIndex(candles, candleTime, time);
        TradeCandle candle = candles[index];
        HighLow highLow = HighLowCandleLeftSearch.getHighNextLow(candles, initN, continueN, index, maxNextInit + 1, candle.getHigh());
        VpgDataTimeRange range = new VpgDataTimeRange();
        range.openTime = candles[highLow.getHighIndex()].getOpenTime();
        range.closeTime = candles[highLow.getLowIndex()].getCloseTime();
        range.startIndex = highLow.getHighIndex();
        range.end = highLow.getLowIndex()+1;
        return range;
    }

    //피보나치 데이터 활용 (파동분석용)
    @Override
    public void setFibonacci() {
        //236 382 // 618
    }


    @Override
    //저점 상승물량 ( 고점에서 물린 물량이 저잠에서 많이 거래되었다면 손바뀜 매집물량으로 계산)
    public BigDecimal getReverseVolume(TradeCandle [] candles, long time){

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
            if(compare.getHigh().compareTo(close) < 0 ){
                sum = sum.add(compare.getVolume());
            }
        }

        // 여기는 연구하면서 변경
        // 의도는 반전 거래량에 매집 물량이 포함되었다면이란 전제
        // 반전 거래량 + 거래량 강도에 의한 예상 거래량이 매물대 돌파를 뚫은만한지를 분석
        for(VpgData data : dataArray){
            if(data.getPrice().compareTo(close) >= 0){
                break;
            }
            sum = sum.add(data.getVolume());
        }

        return sum;
    }



}
