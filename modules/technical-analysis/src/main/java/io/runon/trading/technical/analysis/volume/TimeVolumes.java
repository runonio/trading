package io.runon.trading.technical.analysis.volume;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import io.runon.trading.TimePrice;

/**
 * 시간대별 거래량 정보
 * @author macle
 */
@Data
public class TimeVolumes implements TimePrice{

    long time;
    BigDecimal price;
    BigDecimal priceFutures;

    BigDecimal changeRate;
    BigDecimal changeRateFutures;


    BigDecimal avg5s;
    BigDecimal avg1m;

    final Map<Long, VolumeData> volumeDataMap = new HashMap<>();

    /**
     *
     * @param standardTime 기준시간 (5초 30초 1분... 24시간)
     * @param volume 거래량 정보
     */
    public void put(long standardTime, VolumeData volume){
        volumeDataMap.put(standardTime, volume);
    }

    public VolumeData get(long standardTime){
        return volumeDataMap.get(standardTime);
    }
    @Override
    public long getTime() {
        return time;
    }

    @Override
    public BigDecimal getClose(){return priceFutures;}

}
