package io.runon.trading.oi;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.runon.trading.TradingGson;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 롱숏비율
 * @author macle
 */
public class LongShortRatioData implements LongShortRatio {

    long time;

    BigDecimal longAccount;
    BigDecimal shortAccount;
    BigDecimal ratio;


    public LongShortRatioData(){

    }

    public LongShortRatioData(long time, BigDecimal longAccount, BigDecimal shortAccount){
        this.time = time;
        this.longAccount = longAccount;
        this.shortAccount = shortAccount;
        ratio = longAccount.divide(shortAccount, MathContext.DECIMAL128);

    }
    public LongShortRatioData(long time, BigDecimal longAccount, BigDecimal shortAccount, BigDecimal ratio){
        this.time = time;
        this.longAccount = longAccount;
        this.shortAccount = shortAccount;
        this.ratio = ratio;
    }

    public LongShortRatioData(long time, BigDecimal ratio){
        this.time = time;
        this.ratio = ratio;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setLongAccount(BigDecimal longAccount) {
        this.longAccount = longAccount;
    }

    public void setShortAccount(BigDecimal shortAccount) {
        this.shortAccount = shortAccount;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public BigDecimal getLongAccount() {
        return longAccount;
    }

    @Override
    public BigDecimal getShortAccount() {
        return shortAccount;
    }

    @Override
    public BigDecimal getRatio() {
        return shortAccount;
    }


    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
