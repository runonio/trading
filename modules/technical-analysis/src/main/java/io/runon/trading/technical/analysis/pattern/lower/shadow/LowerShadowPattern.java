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
package io.runon.trading.technical.analysis.pattern.lower.shadow;

import io.runon.trading.TradingBigDecimal;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.pattern.CandlePatternPoint;
import io.runon.trading.technical.analysis.trend.line.TrendLine;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 아래 그림자 캔들
 * @author macle
 */
public class LowerShadowPattern {


    /**
     * 아래 그림자 캔들 유효성 검사
     * @param tradeCandle TradeCandle 캔들
     * @return boolean 유효성 여부
     */
    public static boolean isValid(TradeCandle tradeCandle){

        //아래 그림자가 아니면
        if(tradeCandle.getType() != CandleStick.Type.LOWER_SHADOW){
            return false;
        }

        //몸통길이 계산하기
        //몸통길이는 종가 - 시가
        //몸통 길이 (변화량의 절대값)
        BigDecimal absChange = tradeCandle.changeAbs();

        //몸통이 아래꼬리보다 긴걸로 계산한다
        //양봉이면 아래꼬리는
        BigDecimal lowerTail = tradeCandle.getLowerTail();

        if(absChange.multiply(TradingBigDecimal.DECIMAL_2).compareTo(lowerTail) > 0){
            //아래꼬리가 몸통의 2배보다 커야한다
            return false;
        }

        BigDecimal upperTail = tradeCandle.getUpperTail();
        //noinspection RedundantIfStatement
        if(upperTail.multiply(TradingBigDecimal.DECIMAL_2).compareTo(absChange) > 0 ){
            //위꼬리는 몸통보다 2배이상 작아야한다.
            return false;
        }

        return true;
    }

    /**
     * 아래 그림자 캔들 발생여부 및 정형 점수 생성
     * 발생하지 않을경우 null
     * 발생한 패턴지점은 위 유효성을 통과한 걸로 비교한다
     * @param trendLine TrendLine 추세선
     * @param candles TradeCandle [] 캔들 배열
     * @param index int 체크할 index
     * @param shortGapPercent 짧은 캔들 기준 확률
     * @return CandlePatternPoint 패턴 발생 여부 및 정형점수 ( 발생하지 않을경우 null)
     */
    public static CandlePatternPoint makePoint(TrendLine trendLine, TradeCandle [] candles, int index, BigDecimal shortGapPercent){

        TradeCandle tradeCandle = candles[index];

        if(!LowerShadowPattern.isValid(tradeCandle)){
            return null;
        }


        BigDecimal trendLineScore= trendLine.score(candles, index, 7 , shortGapPercent);


        if(trendLineScore == null){
            return null;
        }

        if(trendLineScore.compareTo(BigDecimal.ONE) < 0){
            //1.0 미만일 경우는 추세 무효
            return null;
        }

        //추세(상승 or 하락) 점수 최대 가중치 1.5점
        if(trendLineScore.compareTo(TradingBigDecimal.DECIMAL_1_5) > 0){
            trendLineScore = TradingBigDecimal.DECIMAL_1_5;
        }

        //몸통길이 계산하기
        //몸통길이는 종가 - 시가
        //몸통 길이 (변화량의 절대값)
        BigDecimal absChange = tradeCandle.changeAbs();

        BigDecimal lowerTail = tradeCandle.getLowerTail();

        BigDecimal score = lowerTail.divide(absChange.multiply(TradingBigDecimal.DECIMAL_100), MathContext.DECIMAL128).multiply(trendLineScore);
        return new CandlePatternPoint(candles[index], score);
    }


}
