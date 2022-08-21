package io.runon.trading.technical.analysis.indicator.volume.profile;

import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * 매물대 분석
 * 매물대란 일정 기간 동안 일정 가격대에서의 누적거래량을 말합니다.
 * 매물대차트는 매물대를 차트상에서 가로 막대 형태로 나타낸 것 입니다.
 * 매물대차트에선 해당 기간동안 이뤄진 전체 거래 대비 특정 매물대가 차지하는 퍼센티지를 나타냅니다.
 * 막대의 길이가 길면 누적거래량이 많다는 뜻이고,
 * 이는 그 가격대가 지지나 저항선을 맡을 수 있는 의미있는 가격대라는 이야기 입니다.
 * @author ccsweets
 */
public class VolumeProfile {
    private static final Integer DEFAULT_VP_SIZE = 10;

    /**
     * 매물대 데이터를 얻는다
     * @param candleArr 가격배열
     * @return 매물대 배열 데이터
     */
    public static VolumeProfileData[] getData(TradeCandle[] candleArr){
        return getData(candleArr, DEFAULT_VP_SIZE);
    }

    /**
     * 매물대 데이터를 얻는다
     * @param candleArr 가격배열
     * @param vpSize 매물대 개수
     * @param arrSize 사용할 가격 배열 수
     * @return 매물대 배열 데이터
     */
    public static VolumeProfileData[] getData(TradeCandle[] candleArr, int vpSize, int arrSize){
        TradeCandle[] newPriceArr = Arrays.copyOfRange(candleArr, candleArr.length - arrSize,  candleArr.length);
        return getData(newPriceArr, vpSize);
    }

    /**
     * 매물대 데이터를 얻는다
     * @param candleArr 가격배열
     * @param vpSize 매물대 개수
     * @return 매물대 배열 데이터
     */
    public static VolumeProfileData[] getData(TradeCandle[] candleArr, int vpSize){
        BigDecimal minPrice = new BigDecimal(Double.MAX_VALUE);
        BigDecimal maxPrice = new BigDecimal(Double.MIN_VALUE);

        for (int i = 0; i < candleArr.length; i++) {
            TradeCandle tradeCandle = candleArr[i];
            BigDecimal close = tradeCandle.getClose();
            if(close.compareTo(minPrice)<0){
                minPrice = close;
            } else if(close.compareTo(maxPrice) > 0){
                maxPrice = close;
            }
        }

        BigDecimal priceRangeTick = maxPrice.subtract(minPrice).divide(new BigDecimal(vpSize), 3, RoundingMode.HALF_UP);
        VolumeProfileData[] vpDataArr = new VolumeProfileData[vpSize];

        for (int i = 0; i < vpSize; i++) {
            VolumeProfileData vpData = new VolumeProfileData();
            BigDecimal priceStartRange = minPrice.add( priceRangeTick.multiply(new BigDecimal(i)) );
            BigDecimal priceEndRange = priceStartRange.add(priceRangeTick);
            vpData.setStartPriceRange(priceStartRange);
            vpData.setEndPriceRange(priceEndRange);
            vpDataArr[i] = vpData;
        }

        BigDecimal totalVolume = new BigDecimal(0);
        BigDecimal totalTradingPrice = new BigDecimal(0);

        for (int i = 0; i < candleArr.length; i++) {
            TradeCandle tradeCandle = candleArr[i];
            totalVolume = totalVolume.add(tradeCandle.getVolume());
            BigDecimal closePrice = tradeCandle.getClose();
            int priceIndex = closePrice.subtract(minPrice).divide(priceRangeTick, RoundingMode.HALF_UP).intValue();
            if(priceIndex == vpSize){
                priceIndex -= 1;
            }
            VolumeProfileData vpData = vpDataArr[priceIndex];
            vpData.setVolume(vpData.getVolume().add(tradeCandle.getVolume()));
            vpData.setTradingPrice(vpData.getTradingPrice().add(tradeCandle.getTradingPrice()));
        }

        for (int i = 0; i < vpDataArr.length; i++) {
            VolumeProfileData vpData = vpDataArr[i];
            BigDecimal volume = vpData.getVolume();
            BigDecimal tradingPrice = vpData.getTradingPrice();
            vpData.setVolumePer( volume.divide(totalVolume, 4, RoundingMode.HALF_UP) );
            vpData.setTradingPricePer( tradingPrice.divide(totalTradingPrice, 4, RoundingMode.HALF_UP));
        }
        return vpDataArr;
    }

    /**
     * 가격에 대한 매물대 데이터를 얻는다.
     * @param vpDataArr 매물대 배열
     * @param price 가격
     * @return 매물대 데이터
     */
    public static VolumeProfileData getVolumeProfile(VolumeProfileData[] vpDataArr, BigDecimal price){
        for (VolumeProfileData vpData : vpDataArr) {
            if(price.compareTo(vpData.getStartPriceRange()) >= 0 && price.compareTo(vpData.getEndPriceRange()) < 0
            ){
                // found
                return vpData;
            }
        }
        return new VolumeProfileData();
    }

}
