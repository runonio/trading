import io.runon.trading.TradingTimes;
import io.runon.trading.YearQuarter;

import java.util.List;

/**
 * @author macle
 */
public class YearQuarterExample {

    public static void main(String[] args) {

        List<YearQuarter> list = TradingTimes.getYearQuarterList(new YearQuarter(2015,1), TradingTimes.KOR_ZONE_ID);

        YearQuarter [] array = TradingTimes.getYearQuarters(new YearQuarter(2015,1), TradingTimes.KOR_ZONE_ID);

        for(YearQuarter yearQuarter : array){
            System.out.println(yearQuarter);
        }

    }
}
