package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 매집 분산 지표
 *  Accumulation/Distribution Indicator
 * chuljoo.tistory.com/136
 *
 * 예전책에는  ((종가 - 저가) - (고가 - 종가)) 부분이 종가 - 시가로 되어있는데 이방법 말고 최근 자료로 구현한다.
 * ((종가 - 저가) - (고가 - 종가)) / (고가 - 저가) * 거래량 + 전일 AD
 *
 * 개요
 * 이 지표는 Larry Williams이 그랜빌의 OBV 개념을 받아들여 수정 개발한 거래량 관련 지표이다.
 * OBV는 당일의 종가와 전일의 종가를 비교하여 당일의 종가가 상승했으면 당일의 거래량을 더해주고, 하락했으면 당일의 거래량을 빼주어 계산한다. 이때의 문제점은 주가 움직임 폭의 강약에 관계없이 동일하게 거래량을 누적시켜 나가기 때문에 주가움직임 강약을 정확히 반영하지 못한다는 점이다.
 * 이러한 단점을 보완하여 L.Williams는 당일의 종가와 시가의 움직임 폭과 당일의 고가와 저가의 움직임 폭을 비교하여 일정비율 만큼의 거래량만을 누적한 지표를 만든 것이다.
 *
 * 해석
 * AD의 해석방법은 OBV와 유사하나 AD는 종가가 전일보다 오르더라도 종가가 시가보다 낮으면 주가 상승탄력이 감소하는 것으로 해석하고, 마찬가지로 종가가 전일보다 내리더라도 종가가 시가보다 높으면 주가 상승탄력이 증가하는 것으로 해석한다
 * AD의 절대적인 수치보다는 고점 혹은 저점의 패턴을 분석하는 것이 더 중요하다. AD 분석에서 가장 중요한 것은 주가와의 괴리도 분석이다.
 *
 * 1) 약세괴리(Bearish Divergence) : 주가는 신고가에 도달했으나 AD는 이전보다 더 낮은 고점을 형성할 때로 매도신호다.
 *
 * 2) 강세괴리(Bullish Divergence) : 주가는 신저가에 도달했으나 AD는 이전보다 높은 곳에서 저점을 형성할 때로 매수신호다.
 *
 * @author macle
 */
public class Adi {

    public static BigDecimal get(TradeCandle candle, BigDecimal previousAd){
        if(candle.getHigh().compareTo(candle.getLow()) == 0){
            return previousAd;
        }

        BigDecimal cl = candle.getClose().subtract(candle.getLow());
        BigDecimal hc = candle.getHigh().subtract(candle.getClose());
        BigDecimal numerator = cl.subtract(hc);
        BigDecimal denominator = candle.getHigh().subtract(candle.getLow());

        return numerator.multiply(candle.getVolume()).divide(denominator, MathContext.DECIMAL128).add(previousAd);
    }

    public static BigDecimal [] getArray(TradeCandle [] array,  int resultLength){
        return getArray(array, BigDecimal.ZERO, array.length - resultLength, array.length);
    }

    public static BigDecimal [] getArray(TradeCandle [] array, BigDecimal initAd, int resultLength){
        return getArray(array, initAd, array.length - resultLength, array.length);
    }

    public static BigDecimal [] getArray(TradeCandle [] array, BigDecimal initAd, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal previousAd = initAd;
        BigDecimal[] adArray = new BigDecimal[resultLength];
        for (int i = 0; i < resultLength; i++) {
            BigDecimal ad = get(array[i+startIndex], previousAd);
            adArray[i] = ad;
            previousAd = ad;
        }

        return adArray;
    }


    public static TimeNumber [] getTimeNumbers(TradeCandle [] array){
        return getTimeNumbers(array, BigDecimal.ZERO, 0, array.length);
    }

    public static TimeNumber [] getTimeNumbers(TradeCandle [] array,  int resultLength){
        return getTimeNumbers(array, BigDecimal.ZERO, array.length - resultLength, array.length);
    }


    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, BigDecimal initAd, int resultLength){
        return getTimeNumbers(array, initAd, array.length - resultLength, array.length);
    }
    public static TimeNumber[] getTimeNumbers(TradeCandle [] array, BigDecimal initAd, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal previousAd = initAd;
        TimeNumber[] adArray = new TimeNumber[resultLength];
        for (int i = 0; i < resultLength; i++) {
            int arrayIndex = i+startIndex;

            BigDecimal ad = get(array[arrayIndex], previousAd);
            adArray[i] = new TimeNumberData(array[arrayIndex].getTime(), ad);
            previousAd = ad;
        }

        return adArray;
    }


    public static TimeNumber [] getNTimeNumbers(TradeCandle [] array, int n){
        return getNTimeNumbers(array, n, 0, array.length);
    }

    public static TimeNumber [] getNTimeNumbers(TradeCandle [] array, int n, int resultLength){
        return getNTimeNumbers(array, n, array.length - resultLength, array.length);
    }

    public static TimeNumber[] getNTimeNumbers(TradeCandle [] array, int n, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        TimeNumber[] adArray = new TimeNumber[resultLength];
        for (int i = 0; i < resultLength; i++) {
            int arrayIndex = i+startIndex;

            long time = array[arrayIndex].getTime();
            BigDecimal ad = getN(array, n, arrayIndex);

            adArray[i] = new TimeNumberData(time, ad);
        }



        return adArray;
    }


    public static BigDecimal getN(TradeCandle [] array, int n, int index){

        int end = index +1;
        if(end > array.length){
            end = array.length;
        }

        int startIndex = index - n;

        if(startIndex < 0){
            startIndex = 0;
        }

        BigDecimal adi = BigDecimal.ZERO;

        for (int i = startIndex; i < end; i++) {
            adi = get(array[i], adi);
        }

        return adi;
    }


}
