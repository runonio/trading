package futures;

import io.runon.trading.data.Futures;
import io.runon.trading.data.FuturesData;
import io.runon.trading.data.TradingDataManager;

/**
 * @author macle
 */
public class FuturesListExample {
    public static void main(String[] args) {


        TradingDataManager tradingDataManager = TradingDataManager.getInstance();
        FuturesData futuresData = tradingDataManager.getFuturesData();
        Futures [] array = futuresData.getFutures("KRX", "20241028");
        for(Futures f : array){
            System.out.println(f);
        }

        System.out.println(array.length);
    }
}
