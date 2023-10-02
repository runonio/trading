/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.runon.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;

/**
 * 거래정보
 * @author macle
 */
public class Trade implements TimeNumber{



    /**
     * 거래유형 정의
     */
    public enum Type{
        NONE //알수없음
        , BUY //구매
        , SELL //판매
        
    }

    /**
     * 거래유형
     */
    private Type type;

    /**
     * 거래량
     */
    private BigDecimal volume;

    /**
     * 가격
     */
    private BigDecimal price;

    /**
     * 거래대금
     */
    private BigDecimal tradingPrice = null;


    /**
     * 시간
     */
    private long time;


    public Trade(){

    }

    /**
     * 생성자
     * @param type Type
     * @param price double
     * @param volume double
     * @param time long
     */
    public Trade(Type type, BigDecimal price, BigDecimal volume, long time ){

        if(price == null || volume == null) {
            throw new IllegalArgumentException("price, volume is not null");
        }

        this.type = type;
        this.price = price;
        this.volume = volume;
        this.time = time;
    }

    /**
     * 거래유형 얻기 BUY, SELL
     * @return Type 거래유형
     */
    public Type getType() {
        return type;
    }

    /**
     * 거래량 얻기
     * @return double 거래량
     */
    public BigDecimal getVolume() {
        return volume;
    }

    /**
     * 가격얻기
     * @return double 가격
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 거래시간 얻기
     * @return long 시간
     */
    public long getTime() {
        return time;
    }


    /**
     * 거래대금 얻기
     * 설정하지 않으면 거래량 * 가격
     * 설정하였으면 설정한 가격
     * @return 거래대금
     */
    public BigDecimal getTradingPrice() {
        if(tradingPrice == null){
            tradingPrice = price.multiply(volume);
        }
        return tradingPrice;
    }

    /**
     * 거래대금 설정
     * @param tradingPrice 거래대금
     */
    public void setTradingPrice(BigDecimal tradingPrice) {
        this.tradingPrice = tradingPrice;
    }


    public void setType(Type type) {
        this.type = type;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public BigDecimal getNumber() {
        return price;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}