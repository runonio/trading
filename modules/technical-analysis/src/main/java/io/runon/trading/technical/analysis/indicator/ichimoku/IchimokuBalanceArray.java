/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.runon.trading.technical.analysis.indicator.ichimoku;

import io.runon.trading.technical.analysis.candle.CandleStick;

/**
 * 일목균형 배열정보
 * @author ccsweets
 */
public class IchimokuBalanceArray {

    private int reverseN;
    private int standardN;
    private int laggingN;
    private int leadingN;

    private int signalN;
    //배열길이
    private int length = -1;

    private double perPrice = -1;

    /**
     * 생성자
     */
    public IchimokuBalanceArray(){
        reverseN = IchimokuBalance.DEFAULT_REVERSE_N;
        standardN = IchimokuBalance.DEFAULT_STANDARD_N;
        laggingN = IchimokuBalance.DEFAULT_LAGGING_N;
        leadingN = IchimokuBalance.DEFAULT_LEADING_N;
    }

    /**
     * 전환선 기간
     * @param reverseN n
     */
    public void setReverseN(int reverseN) {
        this.reverseN = reverseN;
    }

    /**
     * 기준선 기간
     * @param standardN n
     */
    public void setStandardN(int standardN) {
        this.standardN = standardN;
    }

    /**
     * 후행스팬 기간
     * @param laggingN n
     */
    public void setLaggingN(int laggingN) {
        this.laggingN = laggingN;
    }

    /**
     * 선행스팬 기간
     * @param leadingN n
     */
    public void setLeadingN(int leadingN) {
        this.leadingN = leadingN;
    }

    /**
     * 시그널 기간
     * @param signalN n
     */
    public void setSignalN(int signalN) {
        this.signalN = signalN;
    }

    /**
     * 만들고자 하는 배열의수
     * 설정하지 않으면 만들 수 잇는 최대수의 배열을 자동으로 산출한다
     * @param length 배열길이
     */
    public void setLength(int length) {
        this.length = length;
    }

    private IchimokuData [] ichimokuDataArray;

    /**
     * 일목균형 데이터 배열
     * @return 일목균형 데이터 배열
     */
    public IchimokuData[] getIchimokuDataArray() {
        return ichimokuDataArray;
    }

    private IchimokuAnalysisData [] ichimokuAnalysisDataArray;

    /**
     * 일목균형 분석 배열 정보 
     * @return 일목균형 분석 배열 정보
     */
    public IchimokuAnalysisData[] getIchimokuAnalysisDataArray() {
        return ichimokuAnalysisDataArray;
    }

    /**
     * 가격변화 배열을 활용한 rsi와 signal 배열 생성
     * @param candleStickArray 가격변화 배열
     */
    public void make(CandleStick[] candleStickArray){
        ichimokuDataArray = IchimokuBalance.getIchimokuDataArray(candleStickArray,reverseN,standardN,laggingN,leadingN);
        ichimokuAnalysisDataArray = IchimokuBalanceAnalysis.getAnalysisDataArray(candleStickArray,ichimokuDataArray);
    }
}
