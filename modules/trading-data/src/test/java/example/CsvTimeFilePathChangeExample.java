package example;

import io.runon.trading.data.csv.CsvTimeFilePathChange;
import io.runon.trading.data.file.TimeName;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * @author macle
 */
public class CsvTimeFilePathChangeExample {
    public static void main(String[] args) {

        CsvTimeFilePathChange pathChange = new CsvTimeFilePathChange();
        pathChange.setType(TimeName.Type.YEAR_1);

        pathChange.outDirs("D:\\data\\cryptocurrency\\futures\\open_interest\\5m","D:\\data\\cryptocurrency\\futures\\open_interest\\5m_copy");

    }
}
