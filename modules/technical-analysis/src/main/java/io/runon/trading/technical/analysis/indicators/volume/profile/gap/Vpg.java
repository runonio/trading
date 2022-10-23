package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * 매물대분석 가격 갭 활용
 * 분석중 클래스
 * Volume Profile Gap
 *
 * 시가 저가 고가를 활용하여 거래량을 나눈다
 * 호가가 뚫리더라도 의미 있는 가격대를 찾는 용도로 활용한다.
 * 실사용성이 확인되면 클래스명이 변경될 수 있음
 *
 * @author macle
 */
public class Vpg {

    private BigDecimal gap = BigDecimal.ONE;
    private BigDecimal gapHalf = BigDecimals.DECIMAL_0_5;

    public void setGap(BigDecimal gap) {
        this.gap = gap;
        gapHalf = gap.divide(BigDecimals.DECIMAL_2, MathContext.DECIMAL128);
    }

    private int scale = -1;

    /**
     * 거래량 소수점 자리수 
     * 반올림
     *
     * 0보다 작은 값을 설정하면 사용하지 사용하지 않음
     *
     * @param scale 소수점 자리수
     */
    public void setScale(int scale) {
        this.scale = scale;
    }


    private Comparator<VpgData> sort = VpgData.SORT_PRICE;

    public void setSort(Comparator<VpgData> sort) {
        this.sort = sort;
    }

    public BigDecimal getPrice(BigDecimal price){

        BigDecimal remainder = price.remainder(gap);
        BigDecimal result = price.subtract(remainder);

        if(remainder.compareTo(gapHalf) >= 0){
            //반올림
            result = result.add(gap);
        }

        return result.stripTrailingZeros();
    }

    public VpgData [] getArray(TradeCandle [] candles){
        return getArray(candles, 0, candles.length);
    }

    public VpgData [] getArray(TradeCandle [] candles, int length){
        return getArray(candles, candles.length - length, candles.length);
    }

    public VpgData [] getArray(TradeCandle [] candles, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > candles.length){
            end = candles.length;
        }

        if(end - startIndex < 1){
            return VpgData.EMPTY_ARRAY;
        }

        Map<String, VpgData> map = new HashMap<>();

        for (int i = startIndex; i < end; i++) {
            TradeCandle candle = candles[i];

            BigDecimal volume = candle.getVolume().divide(BigDecimals.DECIMAL_3, MathContext.DECIMAL128);
            add(map, candle.getHigh(), volume);
            add(map, candle.getLow(), volume);
            add(map, candle.getClose(), volume);
        }

        if(map.size() == 0){
            return VpgData.EMPTY_ARRAY;
        }

        VpgData [] array = map.values().toArray(new VpgData[0]);
        Arrays.sort(array, sort);
        if(scale >= 0){
            for(VpgData data : array){
                data.volume = data.volume.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
            }
        }

        return array;
    }

    public void add(Map<String, VpgData> map, BigDecimal price, BigDecimal volume){

        price = getPrice(price);

        VpgData vpgData = map.get(price.toPlainString());
        if(vpgData == null){
            vpgData = new VpgData();
            vpgData.price = price;
            map.put(price.toPlainString(), vpgData);
        }

        vpgData.count++;
        vpgData.volume = vpgData.volume.add(volume);

    }

}

