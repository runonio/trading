package io.runon.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 시간과 숫자
 * @author macle
 */
@Data
public class TimeNumberData implements TimePrice, TimeNumber {

    long time;
    BigDecimal number;

    public TimeNumberData(){

    }

    public TimeNumberData(long time, BigDecimal number){
        this.time = time;
        this.number = number;
    }


    @Override
    public BigDecimal getClose() {
        return number;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}
