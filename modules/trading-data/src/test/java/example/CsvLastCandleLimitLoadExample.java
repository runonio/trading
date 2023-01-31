package example;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * @author macle
 */
public class CsvLastCandleLimitLoadExample {
    public static void main(String[] args) {
        String path =  "D:\\data\\cryptocurrency\\futures\\candle\\BTCUSDT\\5m";
        TradeCandle [] candles = CsvCandle.load(path, Times.MINUTE_5, 500);
        for (TradeCandle tradeCandle : candles){
            System.out.println(Times.ymdhm(tradeCandle.getOpenTime(), TradingTimes.KOR_ZONE_ID));
        }
    }
}
