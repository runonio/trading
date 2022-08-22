package example;

import io.runon.trading.data.csv.CsvCommon;

/**
 * @author macle
 */
public class LastTimeExample {
    public static void main(String[] args) {
        System.out.println(CsvCommon.getLastOpenTime("data/cryptocurrency/spot/candle/BTCUSDT/1m"));
    }
}
