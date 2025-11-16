

package io.runon.trading;

import io.runon.commons.utils.GsonUtils;

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
    private BigDecimal amount = null;


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
    public BigDecimal getAmount() {
        if(amount == null){
            amount = price.multiply(volume);
        }
        return amount;
    }

    /**
     * 거래대금 설정
     * @param amount 거래대금
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}