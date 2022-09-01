package io.runon.trading.technical.analysis.indicators.ma;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * macd 결과 데이터
 * @author macle
 */
@Data
public class MacdData {

    long time = -1L;

    BigDecimal macd;
    BigDecimal signal;

    public BigDecimal getOscillator(){
        return macd.subtract(signal).stripTrailingZeros();
    }

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}
