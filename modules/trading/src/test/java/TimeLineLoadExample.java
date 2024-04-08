import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.JsonTimeLine;
import io.runon.trading.data.file.TimeLine;
import io.runon.trading.data.file.TimeLines;
import io.runon.trading.data.file.TimeName;

/**
 * @author macle
 */
public class TimeLineLoadExample {
    public static void main(String[] args) {
        String dirPath ="D:\\data\\cryptocurrency\\futures\\order_book\\BTCBUSD";

        TimeName.Type timeNameType = TimeName.Type.HOUR_4;
        TimeLine timeLine = new JsonTimeLine();
        int count= 500;

//        String [] lines = TimeLines.load(dirPath, TimeName.Type.HOUR_4, timeLine, YmdUtil.getTime("20221103", TradingTimes.UTC_ZONE_ID), count);
        String [] lines = TimeLines.load(dirPath, TimeName.Type.HOUR_4, timeLine, -1, count);
        for(String line : lines){
            System.out.println(Times.ymdhm(timeLine.getTime(line), TradingTimes.UTC_ZONE_ID));
        }

        System.out.println(lines.length);

    }
}
