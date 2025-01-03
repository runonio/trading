import io.runon.commons.utils.time.TimeUtil;
import io.runon.commons.utils.time.Times;
import io.runon.commons.utils.time.YmdUtil;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.csv.CsvSymbolCandle;
import io.runon.trading.technical.analysis.candle.IdCandles;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.time.ZoneId;

/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class CsvSymbolCandleExample {
    public static void main(String[] args) {

        long time = System.currentTimeMillis();

        String path = "D:\\data\\cryptocurrency\\futures\\candle";
        ZoneId zoneId = TradingTimes.UTC_ZONE_ID;

        CsvSymbolCandle csvSymbolCandle = new CsvSymbolCandle(path, "1m");

        long startTime = YmdUtil.getTime("20220701",zoneId);
        long endTime = YmdUtil.getTime("20220731",zoneId);

        IdCandles[] symbolCandles = csvSymbolCandle.load(startTime, endTime);
        System.out.println("length: " + symbolCandles.length +", load time: " + (TimeUtil.getTimeValue(System.currentTimeMillis() - time)));

        for(IdCandles symbolCandle : symbolCandles){
            TradeCandle[] candles =  symbolCandle.getCandles();
            System.out.println(symbolCandle.getId() + ", last open time: " + Times.ymdhm(candles[candles.length-1].getOpenTime(), zoneId) + ", length: " + candles.length);
        }



    }
}
