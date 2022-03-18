package io.runon.trading.backtesting;

import io.runon.trading.data.CandleStorage;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.candles.TradeCandles;

/**
 * 백테스팅용 캔들 얻기
 * 메모리에 올려놓고 사용하기
 * 백테스팅 결과를 빠르게 얻기위해 활용
 * @author macle
 */
public class CandlesGetStorage implements CandlesGet{

    private final CandleStorage candleStorage;
    public CandlesGetStorage(){
        candleStorage = new CandleStorage();
    }

    public TradeCandles loadCandle(String path, long time){
        return candleStorage.loadCandle(path, time);
    }

    @Override
    public TradeCandle[] getCandles(long time, long endTime, int count) {
        return candleStorage.getCandles(time, endTime, count);
    }

    public TradeCandle[] getCandles (long time, int count){
        return candleStorage.getCandles(time, count);
    }

}
