package io.runon.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 가격과 수량 구현체
 * @author macle
 */
@Data
public class PriceQuantityData {

    BigDecimal price;
    BigDecimal quantity;

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }

}
