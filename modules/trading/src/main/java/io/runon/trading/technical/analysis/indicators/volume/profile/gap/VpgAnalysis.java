package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;

/**
 * 매물대분석 가격 갭 활용
 * @author macle
 */
@Data
public class VpgAnalysis {
    BigDecimal maxVolume = BigDecimal.ZERO;
    Map<String, VpgData> map;

    BigDecimal priceGap = BigDecimals.DECIMAL_100;


    public VpgAnalysis(Map<String, VpgData> map){
        this.map = map;
        Collection<VpgData> values = map.values();

        for(VpgData data : values){
            if(data.volume.compareTo(maxVolume) > 0){
                maxVolume = data.volume;
            }
        }

        for(VpgData data : values){
            data.percent = data.volume.divide(maxVolume,2, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).stripTrailingZeros();
        }

    }

    public BigDecimal getPercent(TradeCandle candle){
        BigDecimal price = candle.getLow();
        int count = 0;
        BigDecimal sum = BigDecimal.ZERO;

        for (;;) {
            VpgData data = map.get(price.toPlainString());

            if(data != null){
                sum = sum.add(data.getPercent());
                count ++;
            }

            BigDecimal nextPrice = price.add(priceGap);
            if (nextPrice.compareTo(price) == 0) {
                break;
            }

            if (nextPrice.compareTo(candle.getHigh()) >= 0) {
                price = candle.getHigh();
                data = map.get(price.toPlainString());

                if(data != null){
                    sum = sum.add(data.getPercent());
                    count ++;
                }

                break;
            }

            price = nextPrice;
        }

        if(count == 0){
            return BigDecimal.ZERO;
        }

        return sum.divide(new BigDecimal(count),2, RoundingMode.HALF_UP);

    }


    public TimeNumber [] getPercentiles(TradeCandle [] candles){
        TimeNumber [] percentiles = new TimeNumber[candles.length];
        for (int i = 0; i <percentiles.length ; i++) {
            TradeCandle candle = candles[i];
            BigDecimal percent = getPercent(candle);

            percentiles[i] = new TimeNumberData(candle.getTime(), percent);
        }

        return percentiles;
    }


}
