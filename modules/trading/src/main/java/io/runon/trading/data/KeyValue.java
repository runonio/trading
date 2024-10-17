package io.runon.trading.data;

import lombok.Data;
/**
 * @author macle
 */
@Data
public class KeyValue {

    String key;
    String value;

    public KeyValue(){

    }

    public KeyValue(String key, String value){
        this.key = key;
        this.value = value;
    }

}
