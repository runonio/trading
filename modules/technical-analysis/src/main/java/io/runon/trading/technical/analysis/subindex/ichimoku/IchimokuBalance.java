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

package io.runon.trading.technical.analysis.subindex.ichimoku;

import io.runon.trading.technical.analysis.candle.CandleStick;

/**
 *일목균형표는 1935년 일목산인(一目山人)[1]이 미야코신문사재직중 증권시황란 작성을 위해 개발 한 후,
 * 신동전환선(新東轉煥線)[2]이라는 이름으로 발표하면서 세상에 모습을 드러냈다.
 * 7권에 해당하는 일목균형표이지만 현재에는 4권[3]만이 발간되어 있다.
 * 상세는 일목균형표 원전 감수자 서문을 참고할 것을 권한다
 *
 * 봉차트분석법, 사께다5법, 패턴분석, 엘리어트파동론, 이동평균선, 피보나치, MA오실레이터, ROC개념 등이 포괄적으로 내재되어 있는 복합추세예측방법이라 할 수 있다
 *
 * 일목산인은 개잡주는 일목균형표를 적용하기에 적당하지 않다고 보았다는 얘기가 있다.
 *
 * 아울러 일목균형표를 바탕으로 한 종목시황을 올릴 때도 시황자체가 재료가 되어 자연스럽지 않은 주가흐름을 보이는 것을 보고 종목시황을 안 썼다는 얘기가 일목균형표원전에 나온다.
 * 일목산인은 투자기간의 대부분을 단지 3개의 대형우량주만을 가지고 저점에서 사고, 고점에서 파는 것을 반복했다고 한다. 또한 일봉보다는 주봉을 주로 활용했다고 전해지고 있다.
 *
 * 종가를 바탕으로 작성되는 이동평균선과는 다르게 특정기간의 고가와 저가의 평균값을 내어 지표가 작성된다.
 * (이건 볼린저 밴드에 가깝다)
 * 기술적 분석에 사용되는 보조지표 중에서 끝판왕에 해당한다.[4] 대부분의 보조지표가 후행성이거나 동행성을 보이지만,
 * 일목균형표는 선행성까지 갖고 있어서 미래를 예측하는 데에 효과적으로 사용된다.
 * 일목균형표를 이용해 기술적 분석을 주로 하는 애널리스트들은 코스피폭락장이 오기 전에 폭락일을 정확히 예견하는 경우가 많았다.
 * 다만 어떠한 지표이든지 뭐든 미래예측의 부분에 대해서 (선행성 및 후행성) 부분에 대해 맹신해선 안된다.
 * 이는 특히 시간론을 학습 함에 있어 드러나는 부분이며 해당 종목의 저평가 가격 또는 고평가 가격을 알기 위해 분석하는
 * 것이라기 보다는 크게 변동한 날로부터 기준일마다 그 당시 행동을취했던 또는 소문을 들었던
 * 모든 사람들의 행동에 따른 변화가 일어날 확률(매매 승률)이 높다로 생각 하는 것이 옳다.
 * 선물옵션 트레이더인 알바트로스는 박스권매매 즉, 기술적 분석상 비 추세 매매,또는 역추세매매, 박스권 매매, 단타의 달인이었으며,
 * 이런 부분에서는 자신의 의견과 반대 되는 추세추종 지표중 하나인 일목 균형표에 대해 비판적인 견해를 내 놓을 수 밖에 없었다.
 * 투자스타일 상의 견해 차이를 좁히지 못했다는 점에 대해서는 매우 안타까움을 표한다.
 * 추세추종이나 개인 투자 스타일에 따라 일목 균형표는 호불호가 매우 갈린다는 점을 인지 하여야 한다.
 *
 *
 * 전환선
 * (과거 9일간의 최고가+과거 9일간의 최저가)/2
 * 기준선
 * (과거 26일간의 최고가+과거 26일간의 최저가)/2
 * 후행스팬
 * 금일 종가를 26일 전에 기입
 * 선행스팬1
 * (금일 전환선값+금일 기준선값)/2, 이 수치를 26일 후에 기입
 * 선행스팬2
 * (과거 52일간의 최고가+과거 52일간의 최저가)/2, 이 수치를 26일 후에 기입
 * 구름대
 * 선행스팬1과 선행스팬2 사이를 칠하면 띠를 형성하게 되는데, 이것을 구름대라고 한다
 *
 * 이들을 가리켜 지표론이라고 부르기도 한다.
 *
 * @author ccsweets
 */
public class IchimokuBalance {


    public static final int DEFAULT_REVERSE_N = 9;
    public static final int DEFAULT_STANDARD_N = 26;
    public static final int DEFAULT_LAGGING_N = 26;
    public static final int DEFAULT_LEADING_N = 26;



    /**
     * rsi 점수 얻기
     * 특정기간 n은 14일을 권장하므로 기본값 14를 세팅한 값
     * 9, 25도 많이 사용함
     * @param priceChangeRates 가격 변화율 배열
     * @return rsi score (0~100)
     */
    public static double getScore(double[] priceChangeRates) {
        return getScore(priceChangeRates, 14, priceChangeRates.length);
    }

    /**
     * rsi 점수 얻기
     * 구할 수 없을때 -1.0
     * @param priceChangeRates 가격 변화율 배열
     * @param n 특정기간 n
     * @param end 배열의 끝지점
     * @return rsi score ( 0~100)
     */
    public static double getScore(double [] priceChangeRates, int n, int end){
        int upCount = 0;
        int downCount = 0;

        double upSum = 0.0;
        double downSum = 0.0;


        int start = end - n;
        if(start < 0){
            start = 0;
        }

        for (int i = start; i < end; i++) {

            if(priceChangeRates[i] > 0.0){
                upCount ++;
                upSum += priceChangeRates[i];

            }else if(priceChangeRates[i] < 0.0){
                downCount++;
                downSum += priceChangeRates[i];
            }
        }

        if(upCount == 0 ){
            return 0.0;
        }
        if(downCount == 0){
            return 100.0;
        }

        double averageUps = upSum/(double)upCount;
        //- 값이므로 -를 곲함 양수전환
        double averageDowns = downSum/(double)downCount  * -1.0;

        double rs = averageUps / averageDowns;
        double rsi = rs / (1.0 + rs);

        //소수점 4재짜리 까지만 사용하기
        //백분율 이기때문에  * 100의 효과
        return Math.round(rsi * 10000.0) / 100.0;
    }

    /**
     * 최신 데이터 기준으로 rsi 배열읃 얻는다.
     *
     * @param priceChangeRates 가격 변화율 배열
     * @param n 특정기간
     * @param rsiCount 얻고 싶은 rsi 개수
     * @return rsi 배열
     */
    public static double [] getScores(double[] priceChangeRates, int n, int rsiCount){

        if(rsiCount > priceChangeRates.length){
            rsiCount = priceChangeRates.length;
        }

        double [] rsiScores = new double[rsiCount];
        
        int endGap = rsiCount;
        for (int i = 0; i <rsiCount ; i++) {
            rsiScores[i] = getScore(priceChangeRates, n, priceChangeRates.length - endGap--);
        }
        
        return rsiScores;
    }

    /**
     * 현행선 평균값 배열을 구한다. (전환선 , 기준선)
     * (과거 n일간의 최고가+과거 n일간의 최저가)/2
     * @param candleStickArray 캔들스틱배열
     * @param n 전환선 기간
     * @return
     */
    public static double[] getMiddlePriceArray(CandleStick[] candleStickArray, int n) {
        double [] middlePriceArray = new double[candleStickArray.length - n];
        /* n일만큼은 구할수 없다. 과거 n일 만큼의 최고가와 최저가를 사용하기 때문 */
        int insertCnt = 0;
        for (int i = n; i < candleStickArray.length; i++) {
            double maxPrice = -1;
            double minPrice = Integer.MAX_VALUE;
            for (int j = i-n; j < i ; j++) {
                double highPrice = candleStickArray[j].getHigh().doubleValue();
                double lowPrice = candleStickArray[j].getLow().doubleValue();
                if( highPrice > maxPrice) {
                    maxPrice = highPrice;
                }
                if(lowPrice < minPrice){
                    minPrice = lowPrice;
                }
            }
            double middlePrice = (maxPrice + minPrice) / 2.0 ;
            middlePriceArray[insertCnt++] = middlePrice;
        }
        return middlePriceArray;
    }

    /**
     * 후행스팬
     * @param candleStickArray 캔들스틱배열
     * @return
     */
    public static double[] getLaggingArray(CandleStick[] candleStickArray) {
        double [] laggingArray = new double[candleStickArray.length];
        for (int i = 0; i < candleStickArray.length; i++) {
            laggingArray[i] = candleStickArray[i].getClose().doubleValue();
        }
        return laggingArray;
    }

    /**
     * 선행스팬1
     * (금일 전환선값+금일 기준선값)/2, 이 수치를 26일 후에 기입
     * @param reverseArray 전환선배열
     * @param reverseN 전환선기준기간
     * @param standardArray 기준선배열
     * @param standardN 기준선기간
     * @return 선행스팬1
     */
    public static double[] getLeading1Array(double[] reverseArray, int reverseN, double[] standardArray, int standardN) {
        /* 기준선이 전환선보다 크기 때문에 같은 기간으로 기간 보정 */
        int timeFixN = standardN - reverseN;
        double [] leading1Array= new double[standardArray.length];
        for (int i = 0; i < standardArray.length; i++) {
            double reversePrice = reverseArray[i+timeFixN];
            double standardPrice = standardArray[i];
            leading1Array[i] = (reversePrice + standardPrice) / 2.0;
        }
        return leading1Array;
    }

    /**
     * 시간과 일목균형 데이터를 계산
     * @param candleStickArray 가격정보
     * @param reverseN reverseN
     * @param standardN standardN
     * @param laggingN laggingN
     * @param leadingN leadingN
     * @return 일목균형 데이터 배열
     */
    public static IchimokuData[] getIchimokuDataArray(CandleStick[] candleStickArray, int reverseN, int standardN, int laggingN, int leadingN) {

        int candleStickArrayLength = candleStickArray.length;

        IchimokuData [] ichimokuDataArray = new IchimokuData[candleStickArrayLength];
        for (int i = 0; i < candleStickArrayLength; i++) {
            CandleStick candleStick = candleStickArray[i];
            IchimokuData ichimokuData = new IchimokuData();
            ichimokuData.setTime(candleStick.getOpenTime());
            ichimokuDataArray[i] = ichimokuData;
        }

        double[] reverseArray = getMiddlePriceArray(candleStickArray, reverseN);
        double[] standardArray = getMiddlePriceArray(candleStickArray, standardN);
        double[] laggingArray = getLaggingArray(candleStickArray);
        double[] leading2Array = getMiddlePriceArray(candleStickArray, leadingN * 2);

        int reverseNLength = candleStickArrayLength-reverseN;
        for (int i = 0; i < reverseNLength; i++) {
            ichimokuDataArray[i+reverseN].setReverse(reverseArray[i]);
        }

        int standardNLength = candleStickArrayLength-standardN;
        for (int i = 0; i < standardNLength; i++) {
            ichimokuDataArray[i+standardN].setStandard(standardArray[i]);
        }

        for (int i = 0; i < candleStickArrayLength-laggingN; i++) {
            ichimokuDataArray[i].setLagging(laggingArray[i+laggingN]);
        }

        for (int i = 0; i < candleStickArrayLength-leadingN; i++) {
            IchimokuData ichimokuData = ichimokuDataArray[i];
            if(ichimokuData.getReverse() != -1.0 &&
                    ichimokuData.getStandard() != -1.0
            ){
                ichimokuDataArray[i+leadingN].setLeading1( ( ichimokuData.getReverse() + ichimokuData.getStandard() ) / 2 );
            }
        }


        int leading2NLength = candleStickArrayLength - (leadingN * 3);
        for (int i = 0; i < leading2NLength; i++) {
            ichimokuDataArray[i + (leadingN * 3)].setLeading2(leading2Array[i]);
        }

        return ichimokuDataArray;

    }
}
