package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import io.runon.commons.data.BeginEnd;
import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

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

    private int scale = 0;

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

    private BigDecimal priceGap = BigDecimals.DECIMAL_100;

    public void setPriceGap(BigDecimal priceGap) {
        this.priceGap = priceGap;
    }

    private Comparator<VpgData> sort = VpgData.SORT_PRICE;

    public void setSort(Comparator<VpgData> sort) {
        this.sort = sort;
    }


    public VpgData [] getArray(TradeCandle [] candles){
        return getArray(candles, 0, candles.length);
    }

    public VpgData [] getArray(TradeCandle [] candles, int length){
        return getArray(candles, candles.length - length, candles.length);
    }

    public VpgData [] getArray(TradeCandle [] candles, BeginEnd range){
        return getArray(candles, range.getBegin(), range.getEnd());
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

        Map<String, VpgData> map = makeMap(candles, startIndex, end);

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


    public VpgAnalysis analysis(TradeCandle [] candles){
        return analysis(candles, 0, candles.length);
    }

    public VpgAnalysis analysis(TradeCandle [] candles, int startIndex, int end){
        Map<String, VpgData> map = makeMap(candles, startIndex, end);

        VpgAnalysis analysis = new VpgAnalysis(map);
        analysis.setPriceGap(priceGap);
        return analysis;
    }
    public TimeNumber [] analysisNext(TradeCandle [] candles, int n, int length){
        return analysisNext(candles, n, candles.length - length, candles.length);
    }


    public TimeNumber [] analysisNext(TradeCandle [] candles, int n, int begin, int end){

        if(begin < 0){
            begin = 0;
        }

        if(end > candles.length){
            end = candles.length;
        }


        List<TimeNumber> list = new ArrayList<>();
        for (int i = begin; i <end ; i++) {

            int startIndex = i - n;
            if(startIndex < 0){
                startIndex = 0;
            }

            TradeCandle candle = candles[i];

            Map<String, VpgData> map = makeMap(candles, startIndex, i+1);
            VpgAnalysis analysis = new VpgAnalysis(map);
            analysis.setPriceGap(priceGap);


            TimeNumberData timeNumberData = new TimeNumberData(candle.getTime(), analysis.getPercent(candle));
            list.add(timeNumberData);
        }


        return list.toArray(new TimeNumber[0]);
    }


    public  Map<String, VpgData> makeMap(TradeCandle [] candles, int startIndex, int end) {
        Map<String, VpgData> map = new HashMap<>();

        for (int i = startIndex; i < end; i++) {
            TradeCandle candle = candles[i];

            BigDecimal volume = candle.getVolume();

            BigDecimal price = candle.getLow();
            for (; ; ) {
                add(map, price, volume);

                BigDecimal nextPrice = price.add(priceGap);
                if (nextPrice.compareTo(price) == 0) {
                    break;
                }

                if (nextPrice.compareTo(candle.getHigh()) >= 0) {
                    add(map, candle.getHigh(), volume);
                    break;
                }

                price = nextPrice;
            }
        }
        return map;
    }

    public void add(Map<String, VpgData> map, BigDecimal price, BigDecimal volume){

        price = price.setScale(scale, RoundingMode.HALF_UP);

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