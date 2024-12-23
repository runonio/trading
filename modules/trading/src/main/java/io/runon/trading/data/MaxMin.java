package io.runon.trading.data;

import lombok.Getter;

import java.math.BigDecimal;
/**
 * @author macle
 */
@Getter
public class MaxMin {
    String id;
    BigDecimal max = null;
    BigDecimal min = null;

    public MaxMin(){

    }
    public MaxMin(String id){
        this.id = id;
    }

    public void setMax(BigDecimal number){
        if(number == null){
            return;
        }

        if(this.max == null){
            this.max = number;
            return;
        }

        this.max = this.max.max(number);
    }

    public void setMin(BigDecimal number){
        if(number == null){
            return;
        }

        if(this.min == null){
            this.min = number;
            return;
        }

        this.min = this.min.min(number);
    }

    public void set(BigDecimal number){
        setMax(number);
        setMin(number);

    }

}
