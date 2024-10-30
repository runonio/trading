package io.runon.trading.technical.analysis.indicators.market;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.IdCandles;
import io.runon.trading.technical.analysis.candle.IdCandleTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;


import java.math.BigDecimal;

/**
 * Advancing Decline Issue
 *
 * 상승 종목수 - 하락종목수
 * McClellan Oscillator
 * 와 같이 쓰인다.
 * 참조자료
 * www.hi-ib.com/systemtrade/st02090801view10.jsp
 * @author macle
 */
public class AdIssue extends MarketIndicators<TimeNumber> {


    public AdIssue(IdCandles[] idCandles) {
        super(idCandles);

    }
    public AdIssue(IdCandleTimes idCandleTimes) {
        super(idCandleTimes);

    }
    @Override
    public TimeNumber getData(int index) {
        TimeNumberData data = new TimeNumberData();
        long time = times[index];

        data.setTime(time);
        int searchLength = searchIndex(index);

        int advancing = 0;
        int decline = 0;

        for(IdCandles symbolCandle : idCandles){
            TradeCandle[] candles = symbolCandle.getCandles();
            int openTimeIndex = Candles.getOpenTimeIndex(candles, time, searchLength);
            if(openTimeIndex == -1){
                continue;
            }

            TradeCandle candle = candles[openTimeIndex];
            if(minAmount != null &&  candle.getAmount().compareTo(minAmount) < 0) {
                continue;
            }

            BigDecimal change = candle.getChange();
            if(change == null){
                change =candle.getChangeRate();
            }

            if(change == null){
                continue;
            }

            int compare = change.compareTo(BigDecimal.ZERO);

            if(compare == 0){
                continue;
            }

            if(compare > 0){
                advancing++;
            }else{
                decline++;
            }
        }
        data.setNumber(new BigDecimal(advancing- decline));
        return data;
    }


    @Override
    public TimeNumber[] newArray(int startIndex, int end) {
        TimeNumber[] array = new TimeNumber[end - startIndex];

        for (int i = 0; i < array.length ; i++) {
            array[i] = getData(i + startIndex);
        }
        return array;
    }
}
