package io.runon.trading.technical.analysis.indicators.volume.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * 매물대 분석 데이터
 * @author ccsweets
 */
public class VolumeProfileData {
    private BigDecimal startPriceRange = new BigDecimal(0.0);
    private BigDecimal endPriceRange = new BigDecimal(0.0 );
    private BigDecimal volume = new BigDecimal(0);
    private BigDecimal volumePer = new BigDecimal(0.0);
    private BigDecimal tradingPrice = new BigDecimal(0);
    private BigDecimal tradingPricePer = new BigDecimal(0.0);
}
