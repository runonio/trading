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
package io.runon.trading.technical.analysis.pattern;


import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;

/**
 * 캔들패턴 발생지점
 * @author macle
 */
public class CandlePatternPoint {

    public static final CandlePatternPoint[] EMPTY_POINT = new CandlePatternPoint[0];

    private final TradeCandle candle;
    private final BigDecimal score;

    /**
     * 생성자
     * @param candle TradeCandle 캔들
     * @param score double 점수
     */
    public CandlePatternPoint(TradeCandle candle, BigDecimal score){
        this.candle = candle;
        this.score = score;

    }

    /**
     * 캔들얻기
     * @return TradeCandle 트레이드 캔들(거래에 필요한 정보들이 추가로 있는 캔들)
     */
    public TradeCandle getCandle() {
        return candle;
    }

    /**
     * 점수 얻기
     * @return double 점수
     */
    public BigDecimal getScore() {
        return score;
    }
}
