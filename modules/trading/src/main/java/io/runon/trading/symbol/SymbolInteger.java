package io.runon.trading.symbol;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.util.Comparator;

/**
 * 심볼과 숫자
 * @author macle
 */
@Data
public class SymbolInteger {

    public static final Comparator<SymbolInteger> SORT = (o1, o2) -> {

        if (o1.number == o2.number) {
            return o1.symbol.compareTo(o2.symbol);
        }
        return Integer.compare(o1.number, o2.number);
    };

    public static final Comparator<SymbolInteger> SORT_DESC = (o1, o2) -> {

        if (o1.number == o2.number) {
            return o2.symbol.compareTo(o1.symbol);
        }
        return Integer.compare(o1.number, o2.number);
    };

    public static final Comparator<SymbolInteger> SORT_NUMBER_DESC_SYMBOL_ASC = (o1, o2) -> {

        if (o1.number == o2.number) {
            return o1.symbol.compareTo(o2.symbol);
        }
        return Integer.compare(o2.number, o1.number);
    };


    String symbol;
    int number;

    public SymbolInteger(){

    }
    public SymbolInteger(String symbol, int number){
        this.symbol = symbol;
        this.number = number;
    }


    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}
