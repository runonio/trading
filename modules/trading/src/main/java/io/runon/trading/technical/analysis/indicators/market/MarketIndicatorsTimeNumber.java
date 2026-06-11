package io.runon.trading.technical.analysis.indicators.market;

import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.candle.IdCandleTimes;
import io.runon.trading.technical.analysis.candle.IdCandlesGet;
/**
 * 시장 관련 지표
 * @author macle
 */
public abstract class MarketIndicatorsTimeNumber extends MarketIndicators<TimeNumber>{
    public MarketIndicatorsTimeNumber(IdCandlesGet[] idCandles) {
        super(idCandles);
    }

    public MarketIndicatorsTimeNumber(IdCandleTimes idCandleTimes) {
        super(idCandleTimes);
    }

    public MarketIndicatorsTimeNumber(IdCandlesGet[] idCandles, long [] times){
        super(idCandles, times);
    }

    @Override
    protected TimeNumber[] newEmptyArray(int length) {
        return new TimeNumber[length];
    }

}
