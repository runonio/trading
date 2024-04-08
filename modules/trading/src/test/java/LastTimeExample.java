import io.runon.trading.data.csv.CsvTimeFile;

/**
 * @author macle
 */
public class LastTimeExample {
    public static void main(String[] args) {
        System.out.println(CsvTimeFile.getLastOpenTime("D:\\data\\cryptocurrency\\spot\\candle\\BTCUSDT\\1m"));
    }
}
