package io.runon.trading.technical.analysis.indicators.elder;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class ElderRayData {
    long time;
    BigDecimal bullPower;
    BigDecimal bearPower;
}
