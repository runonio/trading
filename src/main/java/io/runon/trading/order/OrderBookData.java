package io.runon.trading.order;

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
        return OrderBook.value(this);
    }

}
