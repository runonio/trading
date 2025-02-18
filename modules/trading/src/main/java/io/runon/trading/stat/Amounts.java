package io.runon.trading.stat;

import io.runon.trading.Time;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 거래대금 관련 통계
 * @author macle
 */
public class Amounts {

    public static TimeNumber [] sum(TradeCandle [] ... array) {

        Map<Long, TimeNumberData> map = new HashMap<>();

        for (int i = 0; i <array.length ; i++) {
            TradeCandle [] candles = array[i];
            for(TradeCandle candle : candles) {
                TimeNumberData timeNumber = map.get(candle.getTime());
                if(timeNumber == null) {
                    timeNumber = new TimeNumberData();
                    timeNumber.setTime(candle.getTime());
                    timeNumber.setNumber(candle.getAmount());
                    map.put(candle.getTime(), timeNumber);
                }else{
                    timeNumber.addNumber(candle.getAmount());
                }
            }

        }

        TimeNumber [] numbers = map.values().toArray(new TimeNumber[0]);
        Arrays.sort(numbers, Time.SORT_ASC);


        return numbers;
    }

}
