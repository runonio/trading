import com.seomse.commons.utils.time.YmdUtil;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.*;

/**
 * @author macle
 */
public class TimeLineAddExample {

    private static final JsonTimeLine JSON_TIME_LINE = new JsonTimeLine();
    public static void main(String[] args) {

        String dirPath ="D:\\data\\cryptocurrency\\futures\\order_book\\BTCBUSD";

        TimeName.Type timeNameType = TimeName.Type.HOUR_4;
        TimeLine timeLine = new JsonTimeLine();
        int count= 500;

//        String [] lines = TimeLines.load(dirPath, TimeName.Type.HOUR_4, timeLine, YmdUtil.getTime("20221103", TradingTimes.UTC_ZONE_ID), count);



        TimeLineLock timeLineOut = LineOutManager.getInstance().get("D:\\data\\temp", JSON_TIME_LINE, new TimeNameImpl(TimeName.Type.HOUR_1));

        long beginTime =  YmdUtil.getTime("20221103", TradingTimes.UTC_ZONE_ID);
        for (int i = 0; i <2000 ; i++) {
            String [] lines = TimeLines.load(dirPath, TimeName.Type.HOUR_4, timeLine, beginTime, count);
            timeLineOut.add(lines);
            beginTime = timeLine.getTime(lines[lines.length-1]) + 1;

        }




    }


}
