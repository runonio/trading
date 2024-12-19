package io.runon.trading.data;

import io.runon.trading.Time;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.data.json.JsonOrgUtils;
import lombok.Data;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author macle
 */
@Data
public class TimeNumbersMap implements Time {

    long time;
    Integer ymd;

    Map<String, BigDecimal[]> numbersMap = null;

    public void addNumbers(String key, BigDecimal [] number){
        if(numbersMap == null){
            numbersMap = new HashMap<>();
        }
        numbersMap.put(key, number);
    }


    public BigDecimal getNumber(String key, int index){
        if(numbersMap == null){
            return null;
        }

        BigDecimal[] numbers = numbersMap.get(key);
        if(numbers == null){
            return null;
        }

        if(numbers.length <= index){
            return null;
        }

        return numbers[index];
    }


    public static TimeNumber[] getTimeNumbers(TimeNumbersMap [] numbersMaps, String key, int index){
        List<TimeNumber> dataList = new ArrayList<>();

        for(TimeNumbersMap timeNumbersMap : numbersMaps){
            BigDecimal number = timeNumbersMap.getNumber(key, index);
            if(number != null){
                dataList.add( new TimeNumberData(timeNumbersMap.getTime(), number));
            }
        }

        TimeNumber[] timeNumbers = dataList.toArray(new TimeNumber[dataList.size()]);
        dataList.clear();
        return timeNumbers;
    }


    public static TimeNumbersMap make(String line){
        TimeNumbersMap timeNumbersMap = new TimeNumbersMap();
        JSONObject jsonObject = new JSONObject(line);
        if(!jsonObject.isNull("t")){
            timeNumbersMap.time =  jsonObject.getLong("t");
            jsonObject.remove("t");
        }else if(!jsonObject.isNull("time")){
            timeNumbersMap.time = jsonObject.getLong("time");
            jsonObject.remove("time");
        }

        if(!jsonObject.isNull("ymd")){
            timeNumbersMap.ymd =  jsonObject.getInt("ymd");
            jsonObject.remove("ymd");
        }

        timeNumbersMap.numbersMap = new HashMap<>();

        Set<String> keys =  jsonObject.keySet();
        for(String key : keys){

            try{


                BigDecimal [] numbers = JsonOrgUtils.getBigDecimals(jsonObject.getJSONArray(key));
                timeNumbersMap.numbersMap.put(key, numbers);

            }catch (Exception ignore){}
        }



        return timeNumbersMap;
    }

    public static TimeNumbersMap [] make(String [] lines){
        TimeNumbersMap [] timeNumbersMaps = new TimeNumbersMap[lines.length];
        for (int i = 0; i <timeNumbersMaps.length ; i++) {
            timeNumbersMaps[i] = make(lines[i]);
        }

        return timeNumbersMaps;
    }

}
