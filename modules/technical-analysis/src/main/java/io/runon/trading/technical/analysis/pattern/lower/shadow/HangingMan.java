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

import io.runon.trading.PriceChangeType;
import io.runon.trading.TrendChangeType;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.pattern.CandlePatternDefault;
import io.runon.trading.technical.analysis.pattern.CandlePatternPoint;
import io.runon.trading.technical.analysis.trend.line.TrendLine;

import java.math.BigDecimal;

/**
 * 교수형 캔들
 * 상승세의 고점에서 출현해야만 하고 꼬리 길이에 비해 ㅁ몸통 크기가 적가나 반대로 꼬리 길이에 비해 몸통
 * 망치형과 동일한 형태이지만 상승세의 고점에서 출현해야만하고 꼬리 길이에 비해 몸통 크기가 작거나 반대로 꼬리 길이에 비해 몸통크기가 길때유효하다
 * -- 몸통길이가 꼬리에비해 2배이상 작거나 혹은 2배이상 커야 하나봄.. 이건 테스트해보면서 체크해야할듯.
 * 음봉이든 양봉이든 고점에서는 의밍있는 하락 반전 또는 조정 신호가 될 수 있다.
 * @author macle
 */
public class HangingMan extends CandlePatternDefault {



    @Override
    public PriceChangeType getPriceChangeType() {
        return PriceChangeType.FALL;
    }

    @Override
    public TrendChangeType getTrendChangeType() {
        return TrendChangeType.REVERSE;
    }

    /**
     * 캔들의 배열이 바뀔 수 있으므로 array 로 직접 받음
     * @param candles TradeCandle [] 캔들 배열
     * @param index int 기준위치
     * @param shortGapRate 짧은 캔들 기준 확률
     * @return CandlePatternPoint 패턴결과
     */
    public CandlePatternPoint getPoint(TradeCandle[] candles, int index, BigDecimal shortGapRate){
        TrendLine trendLine = new TrendLine(TrendLine.Type.UP);
        return LowerShadowPattern.makePoint(trendLine,candles,index, shortGapRate);
    }



}
