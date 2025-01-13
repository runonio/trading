package io.runon.trading.data.daily;

import io.runon.trading.TimeNumber;
import io.runon.trading.TradingGson;
import io.runon.trading.data.json.JsonOutLine;
import io.runon.trading.technical.analysis.volume.Volumes;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 체결데이터 일별
 * (한국 주식 데이터 기준정보)
 * @author macle
 */
@Data
public class VolumePowerDaily implements TimeNumber, JsonOutLine {

    public static final VolumePowerDaily [] EMPTY_ARRAY = new VolumePowerDaily[0];
    public static final Comparator<VolumePowerDaily> SORT = Comparator.comparingInt(o -> o.ymd);

    Long t ;

    int ymd;


    BigDecimal buyVolume;
    BigDecimal sellVolume;

    public static VolumePowerDaily make(String jsonStr){

        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.fromJson(jsonStr, VolumePowerDaily.class);
    }


    @Override
    public String outTimeLineJsonText(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }


    public long getTime(){
        return t;
    }

    public void setTime(long time){
        this.t = time;
    }

    @Override
    public BigDecimal getNumber() {
        return Volumes.getVolumePower(buyVolume, sellVolume);
    }


//    public static TimeNumber [] makeSumArray(VolumePowerDaily [] array, int n){
//
//
//        return makeSumArray(array, n, 0, array.length);
//    }
//
//    public static TimeNumber [] makeSumArray(VolumePowerDaily [] array , int n, int begin, int end){
//
//        int index =0;
//        TimeNumber [] numbers = new TimeNumber[n];
//
//        for (int i = begin; i <end ; i++) {
//            int startIndex = i - n + 1;
//            if(startIndex < 0){
//                startIndex = 0;
//            }
//            BigDecimal sumBuy;
//            BigDecimal sumSell;
//
//            for (int sumIndex = startIndex; sumIndex <= i ; sumIndex++) {
//
//            }
//
//
//
//        }
//
//
//        return numbers;
//    }
}
