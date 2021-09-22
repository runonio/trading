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
package io.runon.trading.technical.analysis.pattern.upper.shadow;

import io.runon.trading.PriceChangeType;
import io.runon.trading.TrendChangeType;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.pattern.CandlePatternDefault;
import io.runon.trading.technical.analysis.pattern.CandlePatternPoint;
import io.runon.trading.technical.analysis.trend.line.TrendLine;

import java.math.BigDecimal;


/**
 * 역망치형캔들
 * 윗꼬리는 몸통보다 길어야하고 몸통은 너무 두껍지 않아야 한다.
 * 몸통은 양봉일때 보다 유효하다.
 * 하락장에서 윗 꼬리가 길고 양봉일때
 * 상승 반전 또는 조정신호 이긴 하지만
 * 출현 위치 및 이전 패턴에 따라 무효화 되는 경우도 많다.
 * @author macle
 */
public class InvertedHammerPattern extends CandlePatternDefault {

    @Override
    public PriceChangeType getPriceChangeType() {
        return PriceChangeType.RISE;
    }

    @Override
    public TrendChangeType getTrendChangeType() {
        return TrendChangeType.REVERSE;
    }


    @Override
    public CandlePatternPoint getPoint(TradeCandle[] candles, int index, BigDecimal shortGapRate) {

        TradeCandle tradeCandle = candles[index];


        //시점의 가격이 마지막 가격보다 낮으면 음봉
        if(tradeCandle.getOpen().compareTo(tradeCandle.getClose()) > 0){
            //양봉이 아니면
            return null;
        }

        TrendLine trendLine = new TrendLine(TrendLine.Type.DOWN);

        return UpperShadowPattern.makePoint(trendLine,candles,index, shortGapRate);
    }


}
