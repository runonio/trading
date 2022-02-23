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

package io.runon.trading.technical.analysis.indicator.divergence;

import io.runon.trading.Candle;

/**
 * 다이버젼스
 * @author macle
 */
public class Divergence {
        
    
    public static final int MIN_LENGTH = 5;
    public static final double HOLD_RATE = 0.02;
    public static final int [] CANDLE_COUNT_ARRAY = {
            260 //5*52 52주 거래일수로 계산
            , 200
            , 150
            , 120
            , 90
            , 60
            , 30
            , 25
            , 20
            , 15
            , 10
    };

    public enum Type{
        REGULAR //일반 다이버젼스
        , HIDDEN // 히든 다이버젼스
        , EXAGGERATED //과장된 다이버 젼스
    }


    private DivergenceSignalSearch[] searchArray;
    private int [] candleCountArray = CANDLE_COUNT_ARRAY;

    //0.1 보합 비율
    private double steadyRate = 0.1;

    /**
     * 생성자
     * 변경해야 하는 옵션이 있는경우를 생각하여 객체화함
     * 
     */
    public Divergence(){
        searchArray = new DivergenceSignalSearch[3];
        searchArray[0] = new RegularDivergence();
        searchArray[1] = new HiddenDivergence();
        searchArray[2] = new ExaggeratedDivergence();
    }

    /**
     * 다이버전스 시그널 검색기 설정
     * @param searchArray 다이버젼스 시그널 검색기 
     */
    public void setDivergenceSignalSearchArray(DivergenceSignalSearch[] searchArray) {
        this.searchArray = searchArray;
    }

    /**
     * 고점 저점을 확인하기위한 캔들 건수
     * @param candleCountArray 캔들 건수
     */
    public void setCandleCountArray(int[] candleCountArray) {
        this.candleCountArray = candleCountArray;
    }

    /**
     * 보합 비율 설정 
     * 어느정도 상승 하락 율까지 보합으로 볼건지
     * @param steadyRate 보합율
     */
    public void setSteadyRate(double steadyRate) {
        this.steadyRate = steadyRate;
    }

    
    
    /**
     * 다이버젼스 신호 얻기
     * @param priceCandles 가격정보 캔들 배열
     * @param subIndexCandles 보조지표 캔들 배열
     * @return 다이버젼스 정보 배열 신뢰도가 높은 순서로 정렬되어 있음 index 0이 신뢰도 높음
     */
    public DivergenceSignal[] signal(Candle[] priceCandles, Candle [] subIndexCandles){

        DivergenceSignal lastDivergenceIndex  = null;

        int lastIndex = -1;



        //하락 다이버전스를 먼저 실행시키는건 혹시나 둘다 동시 출현시 하락에 대한 우선순위룰 두기 위함
        //동시출현할일은 없을 것 같지만 검증해보지는 않아서 이렇게 처리
        for (DivergenceSignalSearch divergenceSignal : searchArray){
            //하락 다이버젼스
            DivergenceSignal divergenceIndex = divergenceSignal.fall(priceCandles, subIndexCandles, steadyRate, 250);
            if(divergenceIndex == null){
                continue;
            }

            if(lastDivergenceIndex == null ){
                lastDivergenceIndex = divergenceIndex;
            }else{
                if(lastDivergenceIndex.index < divergenceIndex.index ){
                    lastDivergenceIndex = divergenceIndex;
                }
            }
        }
        
        for (DivergenceSignalSearch divergenceSignal : searchArray){
            //상승 다이버젼스
            DivergenceSignal divergenceIndex = divergenceSignal.rise(priceCandles, subIndexCandles, steadyRate, 250);

            if(divergenceIndex == null){
                continue;
            }

            if(lastDivergenceIndex == null ){
                lastDivergenceIndex = divergenceIndex;
            }else{
                if(lastDivergenceIndex.index < divergenceIndex.index ){
                    lastDivergenceIndex = divergenceIndex;
                }
            }
        }

        
        
        return null;
    }



}
