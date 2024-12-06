package io.runon.trading.technical.analysis.volume;

import io.runon.commons.config.Config;
import io.runon.trading.BigDecimals;
import io.runon.trading.TradingMath;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

/**
 * 거래량 관련 유틸성 클래스
 * @author macle
 */
public class Volumes {

    //100.0 == 100% , 500.0 == 500%
    public static final BigDecimal MAX_VOLUME_POWER = new BigDecimal(Config.getConfig("max.volume.power", "500"));

    /**
     * 체결강도
     *
     * @return 체결강도 얻기
     */
    public static BigDecimal getVolumePower(BigDecimal buyVolume, BigDecimal sellVolume) {
        if (sellVolume.compareTo(buyVolume) == 0) {
            return BigDecimals.DECIMAL_100;
        }

        if (sellVolume.compareTo(BigDecimal.ZERO) == 0) {
            //500%
            return MAX_VOLUME_POWER;
        }

        BigDecimal strength = buyVolume.divide(sellVolume, MathContext.DECIMAL128).multiply(BigDecimals.DECIMAL_100);
        if (strength.compareTo(MAX_VOLUME_POWER) > 0) {
            return MAX_VOLUME_POWER;
        }

        return strength;
    }

    public static BigDecimal getAverage(TradeCandle[] candles, BigDecimal highestExclusionRate) {
        return getAverage(candles, candles.length, highestExclusionRate);
    }

    public static BigDecimal getAverage(TradeCandle[] candles, BigDecimal highestExclusionRate, int startIndex, int end) {
        BigDecimal[] volumes = getVolumes(candles, startIndex, end);
        Arrays.sort(volumes);
        return TradingMath.average(volumes, highestExclusionRate);
    }

    public static BigDecimal getAverage(TradeCandle[] candles, BigDecimal lowestExclusionRate, BigDecimal highestExclusionRate) {
        return getAverage(candles, candles.length, lowestExclusionRate, highestExclusionRate);
    }

    public static BigDecimal getAverage(TradeCandle[] candles, int count, BigDecimal highestExclusionRate) {
        BigDecimal[] volumes = getVolumes(candles, count);
        Arrays.sort(volumes);
        return TradingMath.average(volumes, highestExclusionRate);
    }

    public static BigDecimal getAverage(TradeCandle[] candles, int count, BigDecimal lowestExclusionRate, BigDecimal highestExclusionRate) {
        BigDecimal[] volumes = getVolumes(candles, count);
        Arrays.sort(volumes);
        return TradingMath.average(volumes,lowestExclusionRate, highestExclusionRate);
    }


    public static BigDecimal [] getVolumes(TradeCandle [] candles, int count){
        if (count > candles.length) {
            count = candles.length;
        }

        BigDecimal[] volumes = new BigDecimal[count];

        int index = 0;

        for (int i = candles.length - count; i < candles.length; i++) {
            volumes[index++] = candles[i].getVolume();
        }

        return volumes;
    }
    public static BigDecimal [] getVolumes(TradeCandle [] candles, int startIndex, int end){
        if (end > candles.length) {
            end = candles.length;
        }
        if(startIndex < 0){
            startIndex = 0;
        }

        int length = end - startIndex;
        BigDecimal [] volumes = new BigDecimal[length];
        for (int i = 0; i < length; i++) {
            volumes[i] = candles[i+startIndex].getVolume();
        }
        return volumes;
    }
    public static BigDecimal [] getAmountSum(TradeCandle [] candles, int startIndex, int end){
        if (end > candles.length) {
            end = candles.length;
        }
        if(startIndex < 0){
            startIndex = 0;
        }

        int length = end - startIndex;
        BigDecimal [] amountArray = new BigDecimal[length];
        for (int i = 0; i < length; i++) {
            amountArray[i] = candles[i+startIndex].getAmount();
        }
        return amountArray;
    }

    public static BigDecimal getVolumePower(TradeCandle [] candles){
        return getVolumePower(candles, 0 , candles.length);
    }

    public static BigDecimal getVolumePower(TradeCandle [] candles, int startIndex, int end){

        BigDecimal buyVolume = BigDecimal.ZERO;
        BigDecimal sellVolume = BigDecimal.ZERO;

        if(startIndex < 0){
            startIndex = 0;
        }
        if(end > candles.length){
            end = candles.length;
        }


        for (int i = startIndex; i < end; i++) {
            TradeCandle candle = candles[i];
            buyVolume = buyVolume.add(candle.getBuyVolume());
            sellVolume = sellVolume.add(candle.getSellVolume());
        }

        return getVolumePower(buyVolume,sellVolume);
    }

    /**
     * 체결강도 이동평균 얻기
     * 최근 3분 5일선과 같은 형태를 사용하기 위한 용도
     * @param candles 캔들
     * @param count 합칠 캔들의수
     * @param maCount MovingAverage count
     * @return 체결강도 이동평균
     */
    public static BigDecimal getVolumePowerMovingAverage(TradeCandle [] candles, int count, int maCount){


        if( count > candles.length){
            count = candles.length;
        }

        if(maCount > candles.length){
            maCount = candles.length;
        }

        BigDecimal [] array = new BigDecimal[maCount];
        int index = 0;

        for (int i = candles.length - maCount; i <candles.length ; i++) {
            int end = i +1;
            array[index++] = getVolumePower(candles,end - count, end );
        }
        BigDecimal sum = TradingMath.sum(array);
        return sum.divide(new BigDecimal(maCount),MathContext.DECIMAL128);
    }

    public static BigDecimal[] getVolumes(TradeCandle[] candles){
        return getVolumes(candles, 0, candles.length);
    }


    public static BigDecimal[] getVolumePowers(TradeCandle[] candles){
        return getVolumePowers(candles, 0, candles.length);
    }

    public static BigDecimal[] getVolumePowers(TradeCandle[] candles, int startIndex, int end){
        BigDecimal [] array = new BigDecimal[end-startIndex];
        int index = 0;
        for (int i = startIndex; i <end ; i++) {
            array[index++] = candles[i].getVolumePower() ;
        }
        return array;
    }

    public static BigDecimal sum(TradeCandle[] candles, int count){
        return sum(candles, candles.length - count, candles.length);
    }

    public static BigDecimal sum(TradeCandle[] candles, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > candles.length){
            end = candles.length;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = startIndex; i < end ; i++) {
            sum = sum.add(candles[i].getVolume());
        }
        return sum;
    }

}
