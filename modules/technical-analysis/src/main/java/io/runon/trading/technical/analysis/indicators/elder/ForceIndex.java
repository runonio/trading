package io.runon.trading.technical.analysis.indicators.elder;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.ma.Ema;

import java.math.BigDecimal;

/**
 * 강도 지수는 Alexander Elder가 개발한 지표로 상승 압력과 하락 압력을 측정하는 오실레이터이다.단기 이동평균으로 평활화하면 진입, 청산시점을 정확하게 파악할 수 있고 장기 이동평균으로 평활화하면 상승과 하락의 힘에 변화가 발생하는 시점을 알 수 있다.
 * 금일 종가가 전일 종가보다 높으면 주가변동의 힘은 양의 값이 되고 금일 종가가 전일 종가보다 낮으면 음의 값이 된다.
 * 변동폭이 클수록, 거래량이 많을수록 주가변동의 힘은 더 커진다.
 *
 * 단기 강도 지수
 * 추세추종 지표가 상승, 단기 강도 지수가 0선 하향 돌파 -> 매수추세추종 지표가 하락, 단기 강도 지수가 0선 상향 돌파 -> 매도
 * 
 * 중기 강도 지수
 * 중기 강도 지수가 양의 값 -> 황소 세력의 우세(상승 우위)중기 강도 지수가 음의 값 -> 곰 세력의 우세(하락 우위)중기 강도 지수가 0선 왕복하면 추세가 없으므로 추세추종 매매 기법을 사용하면 안된다.중기 강도 지수의 신고점으로 갱신하면 상승이 지속될 확률이 높다.중기 강도 지수가 신저점으로 갱신하면 하락이 지속될 확률이 높다.
 *
 * 책내용 단기강도 지수 활용
 * 강도 지수의 2일 지수이동평균이 상승추세 전에 음수로 전환되면 매수하라
 * 하락추세 중에 강도 지수의 2일 지수이동평균이 양수로 전환되면 매도 하라
 * 강도 지수의 2일 지수이동평균과 주가 사이에 강세 다이버전스가 발생하면 매수신호 주가는 신저점으로 하락하지만 강도 지수가 저점을 높이는 형태
 * 강도 지수의 2일 지수이동평균과 주가 사이에 약세 다이버전스가 발생하면 매도신호 주가는 신고점으로 상승하지만 강도 지수가 고점을 낮추는 형태
 *
 * 책내용 중기 강도지수 활용
 * 강도 지수의 13일 지수이동평균이 중간선 위로 올ㄹ아오면 황소들이 시장을 장악하고 있고 강도 지수의 13일 지수이동평균이 중간선 아래로 내려가면 곰들이 시장을 장악하고 있다는 의미다.
 * 강도 지수의 13일 지수이동 평균이 중간선 부근에서 오르내리면 추세가 없으므로 추세추종 매매 기법을 활용하지 말라는 경고다.
 *
 * 강도 지수의 13일 지수이동평균이 신고점을 기록하면 상승이 지속될 확률이 높다. 강도지수의 13일 지수이동평균과 주가 사이에 약세 다이버 전스가 강력하면강력한 매도 신호다
 * 자구는 신고점을 기록하지만 지표가 고점을 낮추면 황소들이 기운이 없어지고 곰들이 시장을 장악할 태세를 갖추고 있다는 의미다.
 *
 * 강도 지소의 13일 지수이동평균이 신저점으로 떨어지면 하락추세가 계속된다 주가는 신저점으로 떨어지지만 이지표가 저점을 높이면 곰들의 힘이 소진되고 있다는 의미다. 이러한 강세 다이버전스는 강력한 매수 신호다.
 * 
 * @author macle
 */
public class ForceIndex {

    public static final ForceIndexData [] NULL_DATA = new ForceIndexData[0];

    private int shortN = 2;
    private int longN = 13;

    public void setShortN(int shortN) {
        this.shortN = shortN;
    }

    public void setLongN(int longN) {
        this.longN = longN;
    }

    public ForceIndexData [] getArray(TradeCandle[] array, int resultLength) {
        return getArray(array, shortN, longN,array.length - resultLength, array.length);
    }

    public ForceIndexData [] getArray(TradeCandle[] array, int shortN, int longN, int resultLength) {
        return getArray(array, shortN, longN, array.length - resultLength, array.length);
    }

    public ForceIndexData[] getArray(TradeCandle[] array, int shortN, int longN, int startIndex, int end) {

        if(startIndex < 0){
            startIndex = 0;

        }
        if( end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        if(resultLength < 1){
            return NULL_DATA;
        }

        ForceIndexData [] dataArray = new ForceIndexData[resultLength];
        BigDecimal [] forceIndexes = new BigDecimal[resultLength];
        for (int i = 0; i <resultLength ; i++) {
            TradeCandle candle = array[i+startIndex];

            ForceIndexData data = new ForceIndexData();
            data.time = candle.getTime();
            data.forceIndex = candle.getChange().multiply(candle.getVolume());
            forceIndexes[i] = data.forceIndex;
            dataArray[i] = data;
        }

        BigDecimal [] shortIndexes = Ema.getArray(forceIndexes, shortN, forceIndexes.length);
        BigDecimal [] longIndexes  = Ema.getArray(forceIndexes, longN, forceIndexes.length);

        for (int i = 0; i <resultLength ; i++) {
            dataArray[i].shortIndex = shortIndexes[i];
            dataArray[i].longIndex = longIndexes[i];
        }

        return dataArray;
    }

    public static TimeNumber[] getShortIndexes(ForceIndexData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            ForceIndexData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.shortIndex);
        }
        return timeNumbers;
    }

    public static TimeNumber[] getLongIndexes(ForceIndexData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            ForceIndexData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.longIndex);
        }
        return timeNumbers;
    }
}
