package ma;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.TradingTimes;
import io.runon.trading.technical.analysis.indicators.ma.Sma;

import java.math.BigDecimal;
import java.time.ZoneId;

/**
 * SMA 예제
 * @author macle
 */
public class SmaTimeExample {
    public static void main(String[] args) {

        long time = TradingTimes.getOpenTime(Times.MINUTE_1, System.currentTimeMillis());

        TimeNumber[] array = new TimeNumber[10];
        for (int i = 0; i <array.length ; i++) {
            array[i] = new TimeNumberData(time - (Times.MINUTE_1*(array.length - (i +1))), new BigDecimal(i+1));
        }

        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        System.out.println("time: " + Times.ymdhm(time,zoneId));
        for(TimeNumber num : array){
            System.out.println(Times.ymdhm(num.getTime(),zoneId) +": " + num.getNumber().toPlainString());
        }

        TimeNumber[] result = Sma.getTimeNumbers(array, 2,10);

//        TimeNumber[] result = Sma.getTimeMaArray(array, 2,5,9);

        for(TimeNumber avg : result){
            System.out.println(Times.ymdhm(avg.getTime(),zoneId) + " avg: " + avg.getNumber().toPlainString());
        }

    }

}
