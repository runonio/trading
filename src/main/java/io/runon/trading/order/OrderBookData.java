package io.runon.trading.order;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.runon.trading.PriceQuantity;
import lombok.Data;

/**
 * 호가창 구현체
 * @author macle
 */
@Data
public class OrderBookData implements OrderBook{

    long time;

    PriceQuantity [] asks;
    PriceQuantity [] bids;

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }

}
