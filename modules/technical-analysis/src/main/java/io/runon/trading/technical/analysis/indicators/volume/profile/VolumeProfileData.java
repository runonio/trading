package io.runon.trading.technical.analysis.indicators.volume.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 매물대 분석 데이터
 * @author ccsweets
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VolumeProfileData {
    private BigDecimal startPriceRange = BigDecimal.ZERO;
    private BigDecimal endPriceRange = BigDecimal.ZERO;
    private BigDecimal volume = BigDecimal.ZERO;
    private BigDecimal volumePer = BigDecimal.ZERO;
    private BigDecimal tradingPrice = BigDecimal.ZERO;
    private BigDecimal tradingPricePer = BigDecimal.ZERO;
}
