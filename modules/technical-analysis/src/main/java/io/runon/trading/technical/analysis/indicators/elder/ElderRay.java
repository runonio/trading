package io.runon.trading.technical.analysis.indicators.elder;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.indicators.ma.Ema;

import java.math.BigDecimal;

/**
 * 이 지표는 알렉산더 엘더에 의해 1989년에 개발한 지표입니다. 트레이더가 시장의 매수세력과 매도세력의 힘을 볼 수 있도록 하기 위해 지표를 만들었다고 한다. Elder-Ray는 추세추종형 지표의 특성과 오실레이터의 특성을 잘 결합하였다. 이 지표를 만드는 방법은 다음과 같다.
 * 지수이동평균선을 그린다. (저자의 추천은 13일 지수이동평균선)
 *
 * Bull Power와 Bear Power를 계산한다.
 * 매수세력의 강도 = High - EMA(C, 13) 즉, 고가 빼기 13일 지수이동평균값
 *
 * 매도세력의 강도 = Low - EMA(C, 13) 즉, 저가 빼기 13일 지수이동평균값
 *
 * 언제 매도세와 매수세가 강해지고 약해지는가를 알 수 있는 지표이다.
 *
 * 엘더레이 지수는 두가지가 있다.
 *
 * 첫번째는 강세(Bull power)이고, 두번째는 약세(Bear power)이다.
 *
 * 엘더레이 bull power = 금일 고점 - 13일 EMA(지수이동평균값)
 *
 * 엘더레이 bear power = 금일 저점 - 13일 EMA(지수이동평균값)
 *
 *
 * 강세가 양수이고, 약세가 음수인 것이 일반적이다.
 *
 * 강세가 클수록 매수세가 강하다는 것이고, 약세가 깊을수록 매도세가 강하다는 뜻이다.
 *
 * 강세가 양-->음으로 돌아선다면 매도세가 매수세를 압도한 것이고
 *
 * 약세가 음-->양으로 돌아선다면 매수세가 매도세를 압도한 것이다.
 *
 *
 * 최선의 매수신호는 강세다이버전스이다.
 *
 * 최선의 매도신호는 약세 다이버전스이다.
 *
 * 즉 가격은 신저점을 형성하고 있는데, 약세(bear power)가 저점을 높여갈 때,
 *
 * 또는 가격은 신고점을 또는 이중천정을 만들고 있는데, 강세(bull power)가 고점을 낮춰갈 때이다.
 *
 *
 *
 * 매수를 위해서는 두가지 필수조건을 필요하다.
 *
 * 1. 추세가 올라가고 있어야 한다. 이는 EMA가 상승세이거나, 주봉의 추세지표(MACD 등)를 통해서 확인한다.
 *
 * 2. 약세(bear power)가 음수이나, 오르고 있어야 한다.
 *
 * (다음은 이상적인 조건이나 필수는 아니다)
 *
 * 3. 최근의 고점이 이전 고점보다 높아야 한다.
 *
 * 4. 약세(bear power)의 다이버전스(주가는 내려가나, 약세는 상승중)
 * tngks10447.tistory.com/153
 *
 *
 * 심리투자의 법칙 책에서의 매수신호
 * 상승추세여야 한다 (지수이동평균이나 주간 추세추종 지표로 추세확인)
 * 베어파워가 음수이되 상승해야 한다.
 * 볼파워가 가장 최근 고점이 전고점 보다 높다.
 * 베어 파워가 강세 다이버전스를 보인 후 상승 중이다.
 *
 * 매도신호
 * 하락수세여야 한다 (지수이동편균이나 주간 추세추종 지표로 추세확인)
 * 볼파워가 양수이지만 하락중이어야 한다
 * 베어파워의 마지막 지점이 전저점 보다 낮아야 한다
 * 볼파워가 약세 다이버전스를 보인 후 하락하고 있다.
 *
* @author macle
 */
public class ElderRay {

    public static final ElderRayData [] NULL_DATA = new ElderRayData[0];
    private int defaultN = 13;

    public void setDefaultN(int defaultN) {
        this.defaultN = defaultN;
    }

    public ElderRayData [] getArray(CandleStick[] array, int resultLength) {
        return getArray(array, defaultN,array.length - resultLength, array.length);
    }

    public ElderRayData [] getArray(CandleStick[] array, int n, int resultLength) {
        return getArray(array,n,array.length - resultLength, array.length);
    }

    public ElderRayData[] getArray(CandleStick[] array, int n, int startIndex, int end) {

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
        ElderRayData [] dataArray = new ElderRayData[resultLength];
        BigDecimal [] emaArray = Ema.getArray(array, n, startIndex, end);
        for (int i = 0; i <resultLength ; i++) {

            CandleStick candle = array[i+startIndex];

            ElderRayData data = new ElderRayData();
            data.setTime(candle.getTime());

            BigDecimal ema= emaArray[i];

            data.bullPower = candle.getHigh().subtract(ema);
            data.bearPower = candle.getLow().subtract(ema);
            dataArray[i] = data;
        }

        return dataArray;
    }

    public static TimeNumber[] getBullPowers(ElderRayData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            ElderRayData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.bullPower);
        }
        return timeNumbers;
    }

    public static TimeNumber[] getBearPowers(ElderRayData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            ElderRayData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.bearPower);
        }
        return timeNumbers;
    }


}
