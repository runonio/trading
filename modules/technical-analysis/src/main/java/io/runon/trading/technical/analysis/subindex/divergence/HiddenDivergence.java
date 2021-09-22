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

package io.runon.trading.technical.analysis.subindex.divergence;

import io.runon.trading.technical.analysis.candle.Candle;

/**
 * 2. 히든 다이버전스
 *  - 히든 하락 다이버전스
 *   주가의 고점이 낮아지고 보조지표 고점이 상승하는 현상을 히든 하락 다이버전스라고 함
 *   하락추세로 전환 가능성 높음
 *
 *  - 히든 상승 다이버전스
 *   주가의 저점이 높아지고, 보조지표 저점이 하락하는 현상을 히든 상승 다이버전스라고 함
 *   상승추세로 전환 가능성 높음

 * @author macle
 */
public class HiddenDivergence implements DivergenceSignalSearch {

    @Override
    public DivergenceSignal rise(Candle[] priceCandles, Candle [] subIndexCandles, double steadyRate, int candleCount) {
        return null;
    }

    @Override
    public DivergenceSignal fall(Candle[] priceCandles, Candle [] subIndexCandles, double steadyRate, int candleCount) {
        return null;
    }
}
