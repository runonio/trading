import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.candle.CandleDataUtils;

/**
 * @author macle
 */
public class CandlePathTimeZoneChangeExample {
    public static void main(String[] args) {
        CandleDataUtils.changeTimeZone(TradingConfig.getTradingDataPath() + "/commodities");
    }
}
