package io.runon.trading.oi;

import io.runon.trading.TimeDataStorage;

/**
 * 미체결 약정 개별 심볼 데이터
 * @author macle
 */
public class OpenInterestStorageSymbol extends TimeDataStorage<OpenInterest> implements OpenInterestSymbol{

    public void add(OpenInterest data){
        OpenInterest[] newArray;

        if(dataArray.length >= maxLength){
            newArray = new OpenInterest[maxLength];

        }else{
            newArray = new OpenInterest[dataArray.length+1];
        }
        setArray(data, newArray);
    }

    public void add(OpenInterest[] array){
        OpenInterest[] newArray;

        if(array.length > maxLength || dataArray.length + array.length > maxLength) {
            newArray = new OpenInterest[maxLength];

        }else{
            newArray = new OpenInterest[dataArray.length + array.length];
        }
        setArray(array, newArray);

    }
}
