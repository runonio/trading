package io.runon.trading.symbol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 심볼과 숫자
 * @author macle
 */
@Data
public class SymbolNumber {

    public static final Comparator<SymbolNumber> SORT = (o1, o2) -> {
        int compare = o1.number.compareTo(o2.number);

        if (compare == 0) {
            return o1.symbol.compareTo(o2.symbol);
        }
        return compare;
    };

    public static final Comparator<SymbolNumber> SORT_DESC = (o1, o2) -> {
        int compare = o2.number.compareTo(o1.number);

        if (compare == 0) {
            return o2.symbol.compareTo(o1.symbol);
        }
        return compare;
    };

    public static final Comparator<SymbolNumber> SORT_NUMBER_DESC_SYMBOL_ASC = (o1, o2) -> {
        int compare = o2.number.compareTo(o1.number);

        if (compare == 0) {
            return o1.symbol.compareTo(o2.symbol);
        }
        return compare;
    };

    String symbol;
    BigDecimal number;

    public SymbolNumber(){

    }
    public SymbolNumber(String symbol, BigDecimal number){
        this.symbol = symbol;
        this.number = number;
    }


    @Override
    public String toString(){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = new JsonObject();
        if(symbol != null) {
            object.addProperty("symbol", symbol);
        }
        if(number != null) {
            object.addProperty("number", number.stripTrailingZeros());
        }
        return gson.toJson(object);
    }

}
