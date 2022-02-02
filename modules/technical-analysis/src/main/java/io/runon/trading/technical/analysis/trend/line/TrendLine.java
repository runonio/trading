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
package io.runon.trading.technical.analysis.trend.line;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 추세선
 * @author macle
 */
public class TrendLine {


    public enum Type{
        UP
        , DOWN
    }

    private final Type type;

    private TrendLineCase trendLineCase;

    /**
     * 생성자
     * @param type Type 추세선 유형
     */
    public TrendLine(Type type){
        this.type = type;

        switch (type){
            case UP:
                trendLineCase = new TrendLineUp();
                break;
            case DOWN:
                trendLineCase = new TrendLineDown();
                break;
            default:
                break;
        }
    }

    /**
     * 하락 기울기
     * 1.0이면 하락 패턴
     * 1.0 보다 커지면 하락기울기가 가파름
     * @param candles TradeCandle[] 캔듭배열
     * @param index int 기준인덱스
     * @param leftCount int 좌측 건수
     * @param shortGapRate 짧은캔들 비율
     * @return 기울기
     */
    public BigDecimal score(TradeCandle[] candles, int index, int leftCount, BigDecimal shortGapRate ){

        if(index < 5){
            //기울기를 파악하려고하는 최소건수
            return null;
        }

        int minCount = (int)((double)leftCount*0.6);

        if(index < minCount){
            return null;
        }

        int startIndex = index - leftCount;
        if(startIndex  < 0){
            startIndex = 0;
        }

        int shortGapCount = 0;
        int validCount = 0;


        //평균 하락율 구하기
        BigDecimal changeRateSum = BigDecimal.ZERO;


        for(int i=startIndex ; i<index ; i++){

            changeRateSum = changeRateSum.add(candles[i].getChangeRate());

            if(!trendLineCase.isCountValid(candles[i])){
               continue;
            }

            //유효하면
            validCount++;
            if(candles[i].getChangeRate().abs().compareTo(shortGapRate) > 0){
                //종가가 shortGap보다 하락률이 클경우
                shortGapCount++;
            }
        }

        int minShortCount = (int)((double)leftCount*0.45);

        if(validCount < minCount || shortGapCount < minShortCount) {
            //건수 유효성성
            return null;
        }

        int count = index - startIndex;


        BigDecimal avg = changeRateSum.abs().divide(new BigDecimal(count), MathContext.DECIMAL128);
        BigDecimal half = shortGapRate.multiply(BigDecimals.DECIMAL_0_5);

        if(half.compareTo(avg) > 0 ){
            return null;
        }

        return candles[index-1].getClose().subtract(candles[startIndex].getOpen()).abs().divide(new BigDecimal(count).multiply(BigDecimals.DECIMAL_0_5), MathContext.DECIMAL128);
    }

    /**
     * 트렌드 라인 유형
     * 상승 하락
     * @return type up or down
     */
    public Type getType() {
        return type;
    }
}
