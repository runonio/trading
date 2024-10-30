package io.runon.trading.data;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class VolumeAmount {

    BigDecimal volume;
    BigDecimal buyVolume;
    BigDecimal sellVolume;

    BigDecimal amount;
    BigDecimal buyAmount;
    BigDecimal sellAmount;

}
