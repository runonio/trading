package io.runon.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class MaxMinLast {

    public static final MaxMinLast ZERO = new MaxMinLast(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    BigDecimal last;
    BigDecimal max;
    BigDecimal min;

    public MaxMinLast(){}

    public MaxMinLast(BigDecimal max, BigDecimal min, BigDecimal last){
        this.max = max;
        this.min = min;
        this.last = last;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }

}
