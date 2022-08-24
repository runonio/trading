package io.runon.trading.symbol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 심볼과 숫자
 * @author macle
 */
@Data
public class SymbolPriceVolume {
    String symbol;
    BigDecimal price;
    BigDecimal volume;

    @Override
    public String toString(){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = new JsonObject();
        if(symbol != null) {
            object.addProperty("symbol", symbol);
        }
        if(price != null) {
            object.addProperty("price", price.stripTrailingZeros());
        }
        if(volume != null) {
            object.addProperty("volume", volume.stripTrailingZeros());
        }
        return gson.toJson(object);
    }

}
