

package io.runon.trading;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;

/**
 * 가격 변화 구현체
 * @author macle
 */
public class PriceChangeData implements PriceChange{

    BigDecimal close;
    BigDecimal change;
    BigDecimal changeRate;
    BigDecimal previous;


    /**
     * 생성자
     * @param close 종가
     * @param change 변화가격
     * @param changeRate 변화율
     * @param previous 전일가
     */
    public PriceChangeData(
            BigDecimal close
            , BigDecimal change
            , BigDecimal changeRate
            , BigDecimal previous
    ){
        this.close = close;
        this.change = change;
        this.changeRate = changeRate;
        this.previous = previous;
    }

    @Override
    public BigDecimal getClose() {
        return close;
    }

    @Override
    public BigDecimal getChange() {
        return change;
    }

    @Override
    public BigDecimal getChangeRate() {
        return changeRate;
    }

    @Override
    public BigDecimal getPrevious() {
        return previous;
    }
    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}
