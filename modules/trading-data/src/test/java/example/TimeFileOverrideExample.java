package example;

import io.runon.trading.data.file.JsonTimeLine;
import io.runon.trading.data.file.TimeFileOverride;
import io.runon.trading.data.file.TimeLine;
import io.runon.trading.data.file.TimeName;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * @author macle
 */
public class TimeFileOverrideExample {
    public static void main(String[] args) {
        String dirPath = "D:\\data\\cryptocurrency\\futures\\order_book\\BTCUSDT";
        TimeLine timeLine = new JsonTimeLine();

        TimeName.Type timeNameType = TimeName.Type.HOUR_4;
        TimeFileOverride timeFileOverride = new TimeFileOverride(dirPath, timeLine,  timeNameType);
        timeFileOverride.run();

    }
}
