package io.runon.trading.data.time;

import java.time.ZoneId;

/**
 * 데이터 구조에서 일별데이터를 ymd int 형으로 사용할때의 유틸성 매스도
 * @author macle
 */
public class TimeLines {


    public static void updateCandle(String dirPath, ZoneId zoneId, String [] lines ){
        TimeLineOut timeLineOut = LineOutManager.getInstance().getCandleLineOut(dirPath, zoneId);
        timeLineOut.update(lines);
    }
}
