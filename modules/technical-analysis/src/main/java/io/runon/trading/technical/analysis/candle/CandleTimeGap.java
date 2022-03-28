/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.runon.trading.technical.analysis.candle;


import com.seomse.commons.utils.time.Times;
import io.runon.trading.CandleTimes;

import java.util.Calendar;

/**
 * 시간 갭
 * @author macle
 */
public class CandleTimeGap {

    public static final long [] DEFAULT_MINUTES = {

            Times.MINUTE_1
            , Times.MINUTE_2
            , Times.MINUTE_3
            , Times.MINUTE_4
            , Times.MINUTE_5
            , Times.MINUTE_6
            , Times.MINUTE_10
            , Times.MINUTE_12
            , Times.MINUTE_15
            , Times.MINUTE_30
    };

    public static final long [] DEFAULT_HOURS = {
            Times.HOUR_1 //1시간
            , Times.HOUR_2 // 2시간
            , Times.HOUR_3 // 3시간
            , Times.HOUR_4 // 4시간
            , Times.HOUR_6 // 6시간
            , Times.HOUR_12 // 12시간
    };

    public static final long [] DEFAULT_DAYS = {
            Times.DAY_1
            , Times.DAY_3
            , Times.DAY_5
    };

    public static final long [] DEFAULT_SCALPING =makeScalpingTime();

    /**
     * 스캘핑에서 사용할 시간
     * @return long []
     */
    private static long [] makeScalpingTime(){
        long [] scalpingTimes = new long[DEFAULT_MINUTES.length + DEFAULT_HOURS.length + DEFAULT_DAYS.length + 1];
        System.arraycopy(DEFAULT_MINUTES, 0, scalpingTimes, 0, DEFAULT_MINUTES.length);
        System.arraycopy(DEFAULT_HOURS, 0, scalpingTimes, DEFAULT_MINUTES.length, DEFAULT_HOURS.length);
        System.arraycopy(DEFAULT_DAYS, 0, scalpingTimes, DEFAULT_MINUTES.length + DEFAULT_HOURS.length, DEFAULT_DAYS.length);
        scalpingTimes[scalpingTimes.length-1] = Times.WEEK_1;
        return scalpingTimes;
    }

    /**
     * 유효한 설정인지 체크
     * @param timeGap long timeGap
     * @return timeGap boolean 유효성
     */
    public static boolean valid(long timeGap){

        if(timeGap < Times.DAY_1){
            //24시간 보다 작은단위 gap 설정 유효성
            return Times.DAY_1 % timeGap == 0;
        }else{
            //그다음은 하루단위의 봉만 생성
            return timeGap % Times.DAY_1 == 0;
        }
    }

    /**
     * 처음 시작할때의 시작시간 얻기
     * 
     * 아래 빙방법으로 사용 2022 3월 29일 Deprecated 됨
     * CandleTimes.getOpenTime(timeGap, firstTradeTime);
     * 
     * @param timeGap long timeGap
     * @param firstTradeTime long tradeStartTime
     * @return long startTime
     */
    @Deprecated
    public static long getStartTime(long timeGap, long firstTradeTime){
        return CandleTimes.getOpenTime(timeGap, firstTradeTime);
        

    }

}
