package io.runon.trading.backtesting.price;

import io.runon.trading.Price;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author macle
 */
public abstract class MapPrice<E extends Price> implements IdPrice {

    protected Map<String, E> priceMap;

    protected long standardTime = 0L;

    //벡테스팅도중 시간값이 바뀌면 가격값이 초기화 될 수 있음
    public void setPrice(String symbol, E price){
        if(priceMap == null){
            priceMap = new HashMap<>();
        }

        priceMap.put(symbol, price);
    }

    public void setPriceMap(Map<String, E> priceMap) {
        this.priceMap = priceMap;
    }

    public void setStandardTime(long standardTime) {
        this.standardTime = standardTime;
    }

    /**
     * 특정종목만 가격이 변했을경우 업데이트
     * @param standardTime 기준시간
     * @param priceMap 변환된 종목정보
     */
    public void updateMap(long standardTime, Map<String, E> priceMap){
        this.standardTime = standardTime;


        Set<String> keys = priceMap.keySet();
        for(String key : keys){
            E e = priceMap.get(key);
            this.priceMap.put(key, e);
        }
    }

    /**
     * 전체 가격정보 변경
     * @param standardTime 기준시간
     * @param priceMap 새로운 기준시간에 대한 가격 정보
     */
    public void changeMap(long standardTime,  Map<String, E> priceMap){
        this.standardTime = standardTime;
        this.priceMap = priceMap;
    }


    @Override
    public BigDecimal getPrice(String id) {
        return priceMap.get(id).getClose();
    }


    public long getStandardTime() {
        return standardTime;
    }

    public Map<String, E> getPriceMap() {
        return priceMap;
    }
}
