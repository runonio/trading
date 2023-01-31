package io.runon.trading.example.ma;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.indicators.ma.Macd;
import io.runon.trading.technical.analysis.indicators.ma.MacdData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
/**
 * Macd 에제
 * @author macle
 */
public class MacdExample {
    public static void main(String[] args) {
        long time = TradingTimes.getOpenTime(Times.MINUTE_1, System.currentTimeMillis());

        TimeNumber[] array = new TimeNumber[100];
        for (int i = 0; i <array.length ; i++) {
            array[i] = new TimeNumberData(time - (Times.MINUTE_1*(array.length - (i +1))), new BigDecimal(i+1));
        }

        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        MacdData[] result = Macd.get(array);

        for(MacdData data : result){
            System.out.println(Times.ymdhm(data.getTime(),zoneId) + " macd: " + data.getMacd().setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
            + ", signal: " + data.getSignal().setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + ", oscillator: " + data.getOscillator().setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()

            );
        }

        System.out.println("length: " + result.length);
    }
}
