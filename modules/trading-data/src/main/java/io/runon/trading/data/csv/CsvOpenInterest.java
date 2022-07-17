package io.runon.trading.data.csv;

import io.runon.trading.LongShortRatio;
import io.runon.trading.OpenInterest;
import io.runon.trading.OpenInterestData;

import java.math.BigDecimal;

/**
 * csv 형태의 미체결 약정 관리
 * @author macle
 */
public class CsvOpenInterest {

    public static String value(OpenInterest openInterest){
        return value(openInterest.getTime(), openInterest.getOpenInterest(), openInterest.getNotionalValue());
    }
    public static String value(long time, BigDecimal openInterest, BigDecimal notionalValue){
        StringBuilder sb = new StringBuilder();
        sb.append(time).append(",").append(openInterest.stripTrailingZeros().toPlainString());
        if(notionalValue != null){
            sb.append(",").append(notionalValue.stripTrailingZeros().toPlainString());
        }
        return sb.toString();
    }

    public static OpenInterest[] loadOpenInterest(String path, int limit){


        return null;
    }

    public static LongShortRatio[] loadLongShortRatio(String path, int limit){


        return null;
    }

    public static OpenInterest make(String csv){
//        ## open interest (미체결 약정)
//        시간(밀리초 유닉스타임)[0],미체결약정[1],미체결약정명목가치(Notional Value of Open Interest)[2]
//## open interest (미체결 약정) 금액이 없는경우
//        시간(밀리초 유닉스타임)[0],미체결약정[1]

        String [] values = csv.split(",");


        OpenInterestData openInterestData = new OpenInterestData();
        openInterestData.setTime(Long.parseLong(values[0]));
        openInterestData.setOpenInterest(new BigDecimal(values[1]));
        if(values.length > 2){
            openInterestData.setOpenInterest(new BigDecimal(values[1]));
        }

        return openInterestData;
    }

    public static LongShortRatio makeLongShortRatio(String csv){

//        ## long short ratio (롱숏 비율) long account / short account
//        시간(밀리초 유닉스타임)[0],ratio[1],long account[2],short account[3]
//
//## long short ratio (롱숏 비율) 계좌정보를 모를경우
//        시간(밀리초 유닉스타임)[0],ratio[1]


        return null;
    }


}
