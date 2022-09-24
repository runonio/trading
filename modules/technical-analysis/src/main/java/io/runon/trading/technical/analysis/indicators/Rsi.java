/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.BigDecimals;
import io.runon.trading.PriceChangeRate;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.CandleBigDecimals;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.indicators.ma.Sma;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * RSI는 일정 기간 동안 주가가 전일 가격에 비해 상승한 변화량과 하락한 변화량의 평균값을 구하여, 상승한 변화량이 크면 과매수로, 하락한 변화량이 크면 과매도로 판단하는 방식이다.
 *
 * 계산 방법은 다음과 같다. 주어진 기간의 모든 날의 주가에 대해서
 *
 * 가격이 전일 가격보다 상승한 날의 상승분은 U(up) 값이라고 하고,
 * 가격이 전일 가격보다 하락한 날의 하락분은 D(down) 값이라고 한다.
 * U값과 D값의 평균값을 구하여 그것을 각각 AU(average ups)와 AD(average downs)라 한다.
 * AU를 AD값으로 나눈 것을 RS(relative strength) 값이라고 한다. RS 값이 크다는 것은 일정 기간 하락한 폭보다 상승한 폭이 크다는 것을 의미한다.
 * 다음 계산에 의하여 RSI 값을 구한다.
 *
 * RSI 계산 공식 :
 * RSI = RS / (1 + RS)
 * 또는, 다음과 같이 구해도 결과는 동일하다.
 * RSI = AU / (AU + AD)
 * 대체로 이 값은 백분율로 나타낸다.
 *
 * 이 지표의 파라메터로는 기간을 며칠 동안으로 할 것인가가 있다. Welles Wilder는 14일을 사용할 것을 권유했다. 대체로 사용되는 값은 9일, 14~15일, 25~28일 등이다.
 *
 * RSI 그래프는 이동평균선을 함께 나타내는 것이 보통이며, 이동평균선을 며칠선으로 할 것인가 역시 파라메터로 주어진다. RSI를 15일에 대하여 구하고 5일 이동평균선을 함께 표시하는 경우 그래프에 (15, 5)라고 표시해주는 것이 일반적이다.
 *
 * 유사한 지표로는 스토캐스틱이 있다. RSI 그래프의 형태는 fast stochastic 과 비슷하게 나온다.
 *
 * 참고자료
 *  -ko.wikipedia.org/wiki/RSI_(%ED%88%AC%EC%9E%90%EC%A7%80%ED%91%9C)
 *
 *
 *
 * U = 전날 가격보다 오늘 상승할때의 상승폭 (up)
 * D = 전달 가격보다 오늘 하락할때의 하락폭 (down)
 * AU = 일정기간 (N) U의 평균값
 * AD = 일정기간 (N) D의 평균값
 * RS = AU / AD
 * RSI =  RS / (1 + RS) =  AU / (AU + AD)
 *
 * RSI 시그널 = RSI 이동평균선
 * 기본적으로 6일을 사용
 *
 * 매매전략
 * 70% 이상이면 과매수
 * 30% 이하이면 과매도
 * RSI 시그널이 상향돌파하면 단기적 매수세가 늘어나는 추세
 * RSI 시그널이 하향돌파하면 단기적 매도세가 늘어나느 추세
 *
 * 단기 투자방법
 * RSI 가 70% 초과하고 RSI 시그널이 하향돌파 (데드크로스) 할 경우 매도
 * RSI 가 30% 밑들고 RSI 시크널이 상향돌파(골든크로스) 할 경우 매수
 *
 * 장기
 * 주가가 장기적인 상승추세일 때에는 70%를 초과하는 경우가 많으므로 50% 아래로 가면 매수
 * 주가가 장기적인 하락추세일때는 50%를 밑도는 경우가 많으므로 50%를 초과할 때 매수
 *
 * 참고자료
 *  - md2biz.tistory.com/400
 *
 *
 *  rsi score 를 먼저 메소드화 하고
 *  장 단기 추세에 관련해서는 추세와 연동한다 (trend line)
 *
 * @author macle
 */
public class Rsi {

    public static final int DEFAULT_N = 14;
    public static final int DEFAULT_SIGNAL = 6;

    /**
     * rsi 점수 얻기
     * 특정기간 n은 14일을 권장하므로 기본값 14를 세팅한 값
     * 9, 25도 많이 사용함
     * @param priceChangeRates 가격 변화율 배열
     * @return rsi score (0~100)
     */
    public static BigDecimal get(PriceChangeRate[] priceChangeRates) {

        BigDecimal[] array= CandleBigDecimals.getChangeRateArray(priceChangeRates);
        return get(array, DEFAULT_N, array.length);
    }



    /**
     * rsi 점수 얻기
     * 특정기간 n은 14일을 권장하므로 기본값 14를 세팅한 값
     * 9, 25도 많이 사용함
     * @param priceChangeRates 가격 변화율 배열
     * @return rsi score (0~100)
     */
    public static BigDecimal get(BigDecimal [] priceChangeRates) {
        return get(priceChangeRates, DEFAULT_N, priceChangeRates.length);
    }

    /**
     * rsi 점수 얻기
     * 구할 수 없을때 -1.0
     * @param priceChangeRates 가격 변화율 배열
     * @param n 특정기간 n
     * @param end 배열의 끝지점
     * @return rsi score ( 0~100)
     */
    public static BigDecimal get(BigDecimal [] priceChangeRates, int n, int end){
        int upCount = 0;
        int downCount = 0;

        BigDecimal upSum = BigDecimal.ZERO;
        BigDecimal downSum = BigDecimal.ZERO;


        int start = end - n;
        if(start < 0){
            start = 0;
        }

        for (int i = start; i < end; i++) {

            if(priceChangeRates[i].compareTo(BigDecimal.ZERO) > 0){
                upCount ++;
                upSum = upSum.add(priceChangeRates[i]);

            }else if(priceChangeRates[i].compareTo(BigDecimal.ZERO) < 0){
                downCount++;
                downSum = downSum.add(priceChangeRates[i]);
            }
        }

        return get(upCount, downCount, upSum, downSum);
    }

    /**
     * rsi 점수 얻기
     * 구할 수 없을때 -1.0
     * @param priceChangeRates 가격 변화율 배열
     * @param n 특정기간 n
     * @param end 배열의 끝지점
     * @return rsi score ( 0~100)
     */

    public static BigDecimal get(PriceChangeRate [] priceChangeRates, int n, int end){

        int start = end - n;
        if(start < 0){
            start = 0;
        }

        int upCount = 0;
        int downCount = 0;

        BigDecimal upSum = BigDecimal.ZERO;
        BigDecimal downSum = BigDecimal.ZERO;
        for (int i = start; i < end; i++) {
            BigDecimal cr = priceChangeRates[i].getChangeRate();

            if(cr.compareTo(BigDecimal.ZERO) > 0){
                upCount ++;
                upSum = upSum.add(cr);

            }else if(cr.compareTo(BigDecimal.ZERO) < 0){
                downCount++;
                downSum = downSum.add(cr);
            }
        }

        return get(upCount, downCount, upSum, downSum);

    }

    public static BigDecimal get(int upCount, int downCount, BigDecimal upSum, BigDecimal downSum){
        if(upCount == 0 && downCount == 0){
            return new BigDecimal(50);
        }

        if(upCount == 0 ){
            return BigDecimal.ZERO;
        }
        if(downCount == 0){
            return BigDecimals.DECIMAL_100;
        }
        //  U = 전날 가격보다 오늘 상승할때의 상승폭 (up)
        //  D = 전달 가격보다 오늘 하락할때의 하락폭 (down)
        //  AU = 일정기간 (N) U의 평균값
        //  AD = 일정기간 (N) D의 평균값
        //  RS = AU / AD
        //  RSI =  RS / (1 + RS) =  AU / (AU + AD)
        BigDecimal au = upSum.divide(new BigDecimal(upCount), MathContext.DECIMAL128);
        //- 값이므로 -를 곲함 양수전환
        BigDecimal ad = downSum.multiply(BigDecimals.DECIMAL_M_1).divide(new BigDecimal(downCount),MathContext.DECIMAL128);

        BigDecimal rs = au.divide(ad, MathContext.DECIMAL128);
        BigDecimal rsi = rs.divide(BigDecimal.ONE.add(rs), MathContext.DECIMAL128);

        //소수점 4재짜리 까지만 사용하기
        //백분율 이기때문에  * 100의 효과
        return rsi.multiply(BigDecimals.DECIMAL_100);
    }


    /**
     * 최신 데이터 기준으로 rsi 배열읃 얻는다.
     *
     * @param array 가격 변화율 배열
     * @param resultLength 얻고 싶은 rsi 개수
     * @return rsi 배열
     */
    public static BigDecimal [] getArray(BigDecimal[] array,  int resultLength){
        return getArray(array, DEFAULT_N,array.length - resultLength, array.length );
    }


    /**
     * 최신 데이터 기준으로 rsi 배열읃 얻는다.
     *
     * @param array 가격 변화율 배열
     * @param n 특정기간
     * @param resultLength 얻고 싶은 rsi 개수
     * @return rsi 배열
     */
    public static BigDecimal [] getArray(BigDecimal[] array, int n, int resultLength){

        return getArray(array, n,array.length - resultLength, array.length );
    }


    public static BigDecimal [] getArray(BigDecimal[] array, int n, int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal [] rsiScores = new BigDecimal[resultLength];

        for (int i = 0; i <resultLength ; i++) {
            rsiScores[i] = get(array, n, i + startIndex + 1);
        }

        return rsiScores;
    }

    public static BigDecimal [] getArray(PriceChangeRate[] array,  int resultLength){
        return getArray(array, DEFAULT_N,array.length - resultLength, array.length );
    }


    /**
     * 최신 데이터 기준으로 rsi 배열읃 얻는다.
     *
     * @param array 가격 변화율 배열
     * @param n 특정기간
     * @param resultLength 얻고 싶은 rsi 개수
     * @return rsi 배열
     */
    public static BigDecimal [] getArray(PriceChangeRate[] array, int n, int resultLength){
        return getArray(array, n,array.length - resultLength, array.length );
    }

    public static BigDecimal [] getArray(PriceChangeRate[] array, int n, int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        BigDecimal [] rsiScores = new BigDecimal[resultLength];

        for (int i = 0; i <resultLength ; i++) {
            rsiScores[i] = get(array, n, i + startIndex + 1);
        }

        return rsiScores;
    }

    /**
     * 최신 데이터 기준으로 rsi 배열읃 얻는다.
     *
     * @param array 가격 변화율 배열
     * @param n 특정기간
     * @param resultLength 얻고 싶은 rsi 개수
     * @return rsi 배열
     */
    public static TimeNumber[] getTimeNumbers(CandleStick[] array, int n, int resultLength){
        return getTimeNumbers(array, n,array.length - resultLength, array.length );
    }

    public static TimeNumber [] getTimeNumbers(CandleStick[] array, int n, int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;
        TimeNumber [] rsiScores = new TimeNumber[resultLength];

        for (int i = 0; i <resultLength ; i++) {
            int index = i + startIndex;
            rsiScores[i] = new TimeNumberData(array[i].getTime(), get(array, n, index + 1));
        }

        return rsiScores;
    }

    /**
     * rsi signal
     * 이평선
     * 보통 6일을 많이 사용함
     * @param rsiArray rsi 배열
     * @return rsi signal 배열
     */
    public static BigDecimal [] getSignal(BigDecimal [] rsiArray){
        return Sma.getArray(rsiArray, DEFAULT_SIGNAL, rsiArray.length - (DEFAULT_SIGNAL - 1));
    }

    /**
     * rsi signal 
     * 이평선 
     * 보통 6일을 많이 사용함
     * @param rsiArray rsi 배열
     * @param n 특정기간 ( 몇일로 평균을 만들건지) 보통 6일을 많이 사용함
     * @param signalCount 얻고 싶은 signal 개수
     * @return rsi signal 배열
     */
    public static BigDecimal [] getSignal(BigDecimal [] rsiArray, int n, int signalCount){
        return Sma.getArray(rsiArray, n, signalCount);
    }



}
