import io.runon.commons.utils.time.Times;
import io.runon.trading.BeginEndTimeCallback;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.JsonTimeLine;
import io.runon.trading.data.file.TimeLine;
import io.runon.trading.data.file.TimeMissingDataSearch;
import io.runon.trading.data.file.TimeName;
/**
 * @author macle
 */

public class TimeMissingDataSearchExample {
    public static void main(String[] args) {
        TimeLine timeLine = new JsonTimeLine();
        TimeName.Type timeNameType = TimeName.Type.DAY_5;

        String dirPath = "D:\\data\\cryptocurrency\\merge\\volume";

        TimeMissingDataSearch missingDataSearch = new TimeMissingDataSearch(dirPath,  timeLine, timeNameType);

        BeginEndTimeCallback callback = beginEndTime -> {
            System.out.println(Times.ymdhm(beginEndTime.getBeginTime(), TradingTimes.KOR_ZONE_ID) + " \t" + Times.ymdhm(beginEndTime.getEndTime(),TradingTimes.KOR_ZONE_ID));
        };

        missingDataSearch.search(-1L,-1L, callback);


    }
}
