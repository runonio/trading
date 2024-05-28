package io.runon.trading.data;

import lombok.Data;

import java.util.Comparator;

/**
 * 객체와 integer 형태
 * @author macle
 */
@SuppressWarnings("rawtypes")
@Data
public class DataInteger<T> {

    public final static Comparator<DataInteger> SORT_DESC = (o1, o2) -> Integer.compare(o2.value, o1.value);
    public final static Comparator<DataInteger> SORT_ASC = Comparator.comparingInt(o -> o.value);


    T data;
    int value;


    public DataInteger(){

    }

    public DataInteger(T data, int value){
        this.data = data;
        this.value = value;
    }


    public void setValue(String value){
        this.value = Integer.parseInt(value);
    }


}
