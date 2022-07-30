package io.runon.trading.example.ma;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.CandleTimes;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.indicator.ma.Ema;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;

/**
 * EMA 예제
 * @author macle
 */
public class EmaTimeExample {
    public static void main(String[] args) {
        long time = CandleTimes.getOpenTime(Times.MINUTE_1, System.currentTimeMillis());

        TimeNumber[] array = new TimeNumber[100];
        for (int i = 0; i <array.length ; i++) {
            array[i] = new TimeNumberData(time - (Times.MINUTE_1*(array.length - (i +1))), new BigDecimal(i+1));
        }

        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        TimeNumber[] result = Ema.getTimeMaArray(array,14,50);

        for(TimeNumber avg : result){
            System.out.println(Times.ymdhm(avg.getTime(),zoneId) + " avg: " + avg.getNumber().setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        }
    }
}
