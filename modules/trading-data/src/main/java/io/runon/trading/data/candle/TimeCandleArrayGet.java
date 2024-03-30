package io.runon.trading.data.candle;

import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * 백테스팅같은 환경에서 캔들을 메모리에 로딩해놓고 시간에 맞는 캔들만 불러올때
 *
 * @author macle
 */
public class TimeCandleArrayGet implements TimeCandleGet{

    private final TradeCandle [] candles;
    private final long candleTime;

    public TimeCandleArrayGet(TradeCandle [] candles, long candleTime){
        this.candles = candles;
        this.candleTime = candleTime;
    }


    @Override
    public TradeCandle getCandle(long time) {

        long openTime = TradingTimes.getOpenTime(candleTime, time);
        int index  = Candles.getOpenTimeIndex(candles, candleTime,openTime);

        if(index < 0){
            return null;
        }

        return candles[index];
    }
}
