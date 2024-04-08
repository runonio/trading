import com.seomse.commons.utils.time.Times;
import io.runon.trading.data.csv.CsvOpenInterest;
import io.runon.trading.oi.OpenInterest;
import io.runon.trading.oi.OpenInterestSymbolStorage;

/**
 * 비트코인 미체결 약정 예제
 * @author macle
 */
public class BtcOpenInterest {
    public static void main(String[] args) {
        OpenInterestSymbolStorage openInterestStorage = new OpenInterestSymbolStorage();
        openInterestStorage.setDataTimeGap(Times.MINUTE_5);
        openInterestStorage.add(CsvOpenInterest.loadOpenInterest("D:\\data\\cryptocurrency\\futures\\open_interest\\5m\\BTCUSDT",  openInterestStorage.getMaxLength()));

        OpenInterest openInterest = openInterestStorage.getData(1661086500010L);

        System.out.println(openInterest.toString());
    }
}
