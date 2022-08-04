package io.runon.trading.technical.analysis.volume;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.runon.trading.Volume;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class VolumeData implements Volume {
    //거래량
    protected BigDecimal volume;
    //체결강도
    protected BigDecimal volumePower;
    //거래대금
    protected BigDecimal tradingPrice;

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}
