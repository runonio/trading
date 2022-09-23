package io.runon.trading.technical.analysis.candle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * technical analysis candle 관련 유틸성 클래스
 * 기술적 분석 캔들 관련  유틸성 매소드 정리
 * @author macle
 */
public class TaCandles {

    public static long [] getTimes(Candles [] array){
        Set<Long> timeSet = new HashSet<>();
        for(Candles candles : array){
            TradeCandle [] tradeCandles = candles.getCandles();
            for(TradeCandle candle: tradeCandles){
                timeSet.add(candle.getTime());
            }
        }

        long [] times = new long[timeSet.size()];
        int index = 0;
        for (long time : timeSet) {
            times[index++] = time;
        }

        Arrays.sort(times);

        return times;
    }

    public static int getOpenTimeIndex(TradeCandle [] candles, long openTime){
        return getOpenTimeIndex(candles, openTime, candles.length);
    }

    public static int getOpenTimeIndex(TradeCandle [] candles, long openTime, int searchLength){

        int startIndex = candles.length - searchLength;
        if(startIndex < 0){
            startIndex = 0;
        }

        for (int i = startIndex; i < candles.length; i++) {
            if(candles[i].getOpenTime() == openTime){
                return i;
            }
        }

        return -1;
    }

    public static int getStartIndex(TradeCandle [] candles, long openTime, int searchLength){

        int startIndex = candles.length - searchLength;
        if(startIndex < 0){
            startIndex = 0;
        }

        for (int i = startIndex; i < candles.length; i++) {
            if(candles[i].getOpenTime() >= openTime){
                return i;
            }
        }
        return -1;
    }




}
