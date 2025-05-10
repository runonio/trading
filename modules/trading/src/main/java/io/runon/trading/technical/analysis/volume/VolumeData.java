package io.runon.trading.technical.analysis.volume;

import io.runon.commons.utils.GsonUtils;
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
    protected BigDecimal amount;

    @Override
    public String toString(){
        return  GsonUtils.LOWER_CASE_WITH_UNDERSCORES_PRETTY.toJson(this);
    }
}
