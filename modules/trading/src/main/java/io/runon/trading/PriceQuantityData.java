package io.runon.trading;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 가격과 수량 구현체
 * @author macle
 */
@Data
public class PriceQuantityData implements PriceQuantity{

    BigDecimal price;
    BigDecimal quantity;

    @Override
    public String toString(){

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(price.stripTrailingZeros().toPlainString());
        jsonArray.add(quantity.stripTrailingZeros().toPlainString());
        return new Gson().toJson(jsonArray);
    }

}
