package io.runon.trading.technical.analysis.indicators.ma;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.runon.trading.TimeNumber;
import lombok.Data;

import java.math.BigDecimal;

/**
 * macd 결과 데이터
 * @author macle
 */
@Data
public class MacdData implements TimeNumber {

    long time = -1L;
    BigDecimal macd;
    BigDecimal signal;
    BigDecimal oscillator;

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }

    @Override
    public BigDecimal getNumber() {
        return macd;
    }
}
