package io.runon.trading.data;

import com.seomse.commons.utils.GsonUtils;

/**
 * @author macle
 */
public class StringArray {

    String [] array;


    public StringArray(String ...array){
        this.array = array;
    }
    public String[] getArray() {
        return array;
    }

    public String get(int index){
        return array[index];
    }

    public int length(){
        return array.length;
    }

    @Override
    public String toString(){
        return GsonUtils.GSON.toJson(array);
    }
}
