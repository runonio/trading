package io.runon.trading.data.file;

import io.runon.commons.callback.StrCallback;
import io.runon.trading.BeginEndTime;
import io.runon.trading.BeginEndTimeCallback;
import io.runon.trading.BeginEndTimeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author macle
 */
public class TimeMissingDataSearch {

    private final String dirPath;
    private final TimeLine timeLine;
    private final TimeName.Type timeNameType;

    public TimeMissingDataSearch(String dirPath
            , TimeLine timeLine
            , TimeName.Type timeNameType){
        this.dirPath = dirPath;
        this.timeLine = timeLine;
        this.timeNameType = timeNameType;
    }



    private long missingTime = 7000L;
    public void setMissingTime(long missingTime) {
        this.missingTime = missingTime;
    }

    //다음버젼에서 처리 휴일 및 거래불가 시간 당장에는 암호화폐에서 사용하므로 휴일이 없음.

    //국가별 타임존 작업과 관련이 있음. off 타임존과 off 시간범위 설정기능 추가 (시 분) offZoneId
//    private DayOfWeek [] offDays = null;
//    public void setOffDays(DayOfWeek[] offDays) {
//        this.offDays = offDays;
//    }
//
//    public void setOffDayWeekend() {
//        offDays = new DayOfWeek[]{
//                DayOfWeek.SATURDAY
//                , DayOfWeek.SUNDAY
//        };
//    }


    public void search(BeginEndTimeCallback callback){
        search(-1, System.currentTimeMillis(), callback);
    }

    public void search(long beginTime, BeginEndTimeCallback callback){
        search(beginTime, System.currentTimeMillis(), callback);
    }

    public void search(long beginTime, long endTime, final BeginEndTimeCallback callback){

        StrCallback strCallback = new StrCallback() {
            long lastTime = -1;

            @Override
            public void callback(String str) {
                long time = timeLine.getTime(str);


                if(lastTime < 0){
                    lastTime = time;
                    return;
                }

                if(time - lastTime  >= missingTime){
                    //차후에 휴장일, 장 종료시간 로직 추가

                    callback.callback(new BeginEndTimeData(lastTime, time));
                }

                lastTime = time;
            }
        };

        TimeLines.load(dirPath, beginTime, endTime, timeNameType, timeLine, strCallback);
    }

    public BeginEndTime [] search(long beginTime, long endTime){

        final List<BeginEndTime> list = new ArrayList<>();

        BeginEndTimeCallback callback = list::add;

        search(beginTime, endTime, callback);

        if(list.size() == 0){
            return BeginEndTime.EMPTY_ARRAY;
        }

        BeginEndTime [] array = list.toArray(new BeginEndTime[0]);
        list.clear();

        return array;
    }

}
