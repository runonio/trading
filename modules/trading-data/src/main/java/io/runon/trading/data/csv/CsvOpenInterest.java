package io.runon.trading.data.csv;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.validation.NumberNameFileValidation;
import io.runon.trading.oi.LongShortRatio;
import io.runon.trading.oi.LongShortRatioData;
import io.runon.trading.oi.OpenInterest;
import io.runon.trading.oi.OpenInterestData;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

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
    public static OpenInterest[] loadOpenInterest(String path) {
        return loadOpenInterest(path, 0);
    }


    public static OpenInterest[] loadOpenInterest(String path, int limit){
        String [] lines = FileUtil.getLines(new File(path), StandardCharsets.UTF_8,new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG, limit);
        OpenInterest [] array = new OpenInterest[lines.length];
        for (int i = 0; i < array.length ; i++) {
            array[i] = make(lines[i]);
        }
        return array;
    }

    public static LongShortRatio[] loadLongShortRatio(String path) {
        return loadLongShortRatio(path, 0);
    }


    public static LongShortRatio[] loadLongShortRatio(String path, int limit){
        String [] lines = FileUtil.getLines(new File(path), StandardCharsets.UTF_8,new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG, limit);
        LongShortRatio [] array = new LongShortRatio[lines.length];
        for (int i = 0; i < array.length ; i++) {
            array[i] = makeLongShortRatio(lines[i]);
        }

        return array;
    }

    public static OpenInterest make(String csv){
        String [] values = csv.split(",");
        OpenInterestData openInterestData = new OpenInterestData();
        openInterestData.setTime(Long.parseLong(values[0]));
        openInterestData.setOpenInterest(new BigDecimal(values[1]));
        if(values.length > 2){
            openInterestData.setOpenInterest(new BigDecimal(values[2]));
        }

        return openInterestData;
    }

    public static LongShortRatio makeLongShortRatio(String csv){
        String [] values = csv.split(",");
        LongShortRatioData longShortRatioData = new LongShortRatioData();
        longShortRatioData.setTime(Long.parseLong(values[0]));
        longShortRatioData.setRatio(new BigDecimal(values[1]));
        if(values.length > 2){
            longShortRatioData.setLongAccount(new BigDecimal(values[2]));
            longShortRatioData.setShortAccount(new BigDecimal(values[3]));
        }

        return longShortRatioData;
    }


}
