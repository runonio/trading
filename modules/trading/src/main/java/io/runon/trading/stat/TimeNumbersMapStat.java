package io.runon.trading.stat;

import io.runon.commons.exception.UndefinedException;
import io.runon.trading.data.TimeNumbersMap;

import java.math.BigDecimal;
import java.util.*;

/**
 * TimeNumbersMap 관련 통계
 * @author macle
 */
public class TimeNumbersMapStat {

    private final TimeNumbersMap[] array;

    private Map<String, TimeNumbersMap[]> sortMap;

    public TimeNumbersMapStat(TimeNumbersMap[] array){
        this.array = array;
    }

    public TimeNumbersMap[] sort(String key, int index, boolean isAsc){

        if(sortMap == null){
            sortMap = new HashMap<>();
        }

        String mapKey = key + "," + index +"," + isAsc;

        TimeNumbersMap[] timeNumbersMaps = sortMap.get(mapKey);
        if(timeNumbersMaps != null){
            return timeNumbersMaps;
        }

        List<TimeNumbersMap> newList = new ArrayList<>();

        for(TimeNumbersMap timeNumbersMap : array){

            BigDecimal number = timeNumbersMap.getNumber(key, index);
            if(number == null){
                continue;
            }

            newList.add(timeNumbersMap);

        }

        if(newList.isEmpty()){
            throw new UndefinedException("key: " + key + ", index: " + index + ", check number null");
        }

//        final BigDecimal minNUmber = min.subtract(BigDecimal.ONE);
//        final BigDecimal maxNumber = max.add(BigDecimal.ONE);

        TimeNumbersMap [] newArray = newList.toArray(new TimeNumbersMap[newList.size()]);

        if(newArray.length == 1){
            sortMap.put(mapKey, newArray);
            return newArray;
        }

        Comparator<TimeNumbersMap> sort = (o1, o2) -> {

            BigDecimal number1 = o1.getNumber(key, index);
            BigDecimal number2 = o2.getNumber(key, index);

            if(isAsc){
//              if(number1 == null){
//                  number1 = maxNumber;
//              }
//
//              if(number2 == null){
//                  number2 = maxNumber;
//              }

              return number1.compareTo(number2);


            }else{
//                if(number1 == null){
//                    number1 = minNUmber;
//                }
//
//                if(number2 == null){
//                    number2 = minNUmber;
//                }

                return number2.compareTo(number1);
            }

        };

        Arrays.sort(newArray, sort);
        sortMap.put(mapKey, newArray);
        return newArray;
    }

}