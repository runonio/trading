package io.runon.trading.order;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.runon.trading.PriceQuantity;

/**
 * 호가창
 * @author macle
 */
public interface OrderBook {
    /**
     * 호가창 시간
     * @return 시간얻기
     */
    long getTime();

    /**
     * @return 매도호가 목록
     */
    PriceQuantity[] getAsks();

    /**
     *
     * @return 매수호가 목록
     */
    PriceQuantity[] getBids();



    static String value(OrderBook orderBook){
        PriceQuantity[] askArray = orderBook.getAsks();
        JsonArray asks = new JsonArray();
        for(PriceQuantity pq : askArray){
            JsonArray ask = new JsonArray();
            ask.add(pq.getPrice().stripTrailingZeros().toPlainString());
            ask.add(pq.getQuantity().stripTrailingZeros().toPlainString());
            asks.add(ask);
        }

        PriceQuantity[] bidArray = orderBook.getBids();
        JsonArray bids = new JsonArray();
        for(PriceQuantity pq : bidArray){
            JsonArray bid = new JsonArray();
            bid.add(pq.getPrice().stripTrailingZeros().toPlainString());
            bid.add(pq.getQuantity().stripTrailingZeros().toPlainString());
            bids.add(bid);
        }

        JsonObject outObj = new JsonObject();
        outObj.addProperty("t", orderBook.getTime());
        outObj.add("asks", asks);
        outObj.add("bids", bids);

        return new Gson().toJson(outObj);
    }
}
