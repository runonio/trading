package example;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.data.csv.CsvOpenInterest;
import io.runon.trading.oi.OpenInterest;
import io.runon.trading.oi.OpenInterestSymbolMergeStorage;
import io.runon.trading.oi.OpenInterestSymbolStorage;

/**
 * 비트코인 미체결 약정 예제
 * @author macle
 */
public class BtcMergeOpenInterest {
    public static void main(String[] args) {
        OpenInterestSymbolStorage usdtOpenInterestStorage = new OpenInterestSymbolStorage();
        usdtOpenInterestStorage.setDataTimeGap(Times.MINUTE_5);
        usdtOpenInterestStorage.add(CsvOpenInterest.loadOpenInterest("D:\\data\\cryptocurrency\\futures\\open_interest\\5m\\BTCUSDT",0));
        OpenInterestSymbolStorage busdOpenInterestStorage = new OpenInterestSymbolStorage();
        busdOpenInterestStorage.setDataTimeGap(Times.MINUTE_5);
        busdOpenInterestStorage.add(CsvOpenInterest.loadOpenInterest("D:\\data\\cryptocurrency\\futures\\open_interest\\5m\\BTCBUSD",  busdOpenInterestStorage.getMaxLength()));

        OpenInterestSymbolStorage [] storages = {usdtOpenInterestStorage, busdOpenInterestStorage};

        OpenInterestSymbolMergeStorage openInterestSymbolMergeStorage = new OpenInterestSymbolMergeStorage(storages);

        OpenInterest openInterest = openInterestSymbolMergeStorage.getData(1661086500010L);

        System.out.println(openInterest.toString());
    }
}
