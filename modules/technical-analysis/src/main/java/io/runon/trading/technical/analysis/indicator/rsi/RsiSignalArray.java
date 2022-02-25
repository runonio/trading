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

package io.runon.trading.technical.analysis.indicator.rsi;

import io.runon.trading.PriceChangeRate;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * rsi 와 signal 배열정보
 * @author macle
 */
public class RsiSignalArray {

    private int n;
    private int signalN;
    //배열길이
    private int length = -1;

    /**
     * 생성자
     */
    public RsiSignalArray(){
        n = Rsi.DEFAULT_N;
        signalN = Rsi.DEFAULT_SIGNAL;
    }

    /**
     * RSI를 구성하는 n의 기간 실정
     * @param n rsi 구성할때 사용하는 수
     */
    public void setN(int n) {
        this.n = n;
    }

    /**
     * signal을 구성할때 사용하는 수
     * @param signalN signal 구성에 사용되는 RSI 수
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


    private BigDecimal [] rsiArray;
    private BigDecimal [] signalArray;

    /**
     * 가격변화 배열을 활용한 rsi와 signal 배열 생성
     * @param priceChangeRates 가격변화 배열
     */
    public void make(PriceChangeRate[] priceChangeRates){
        BigDecimal[] array = new BigDecimal[priceChangeRates.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = priceChangeRates[i].getChangeRate();
        }
        make(array);
    }

    /**
     * 가격변화 배열을 활용한 rsi와 signal 배열 생성
     * @param priceChangeRates 가격변화 배열
     */
    public void make(BigDecimal [] priceChangeRates){

        //만들 수 있는 배열의 수 구하기
        int makeLength = priceChangeRates.length - n - signalN + 2;
        if(length != -1 && makeLength > length){
            makeLength = length;
        }


        BigDecimal [] tempRsiArray = Rsi.getScores(priceChangeRates, n ,makeLength+ signalN - 1);
        signalArray = Rsi.getSignal(tempRsiArray, signalN, makeLength);

        this.rsiArray = Arrays.copyOfRange(tempRsiArray, signalN - 1, tempRsiArray.length);
        

    }

    /**
     * rsi 배열 얻기
     * @return rsi array
     */
    public BigDecimal[] getRsiArray() {
        return rsiArray;
    }

    /**
     * rsi signal 배열 얻기
     * @return signal array
     */
    public BigDecimal[] getSignalArray() {
        return signalArray;
    }

    /**
     *
     * @return last rsi
     */
    public BigDecimal getLastRsi(){
        return rsiArray[rsiArray.length-1];
    }

    /**
     * 
     * @return last signal
     */
    public BigDecimal getLastSignal(){
        return signalArray[signalArray.length-1];
    }

}
