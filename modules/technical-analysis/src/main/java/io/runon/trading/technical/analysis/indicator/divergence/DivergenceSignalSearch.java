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
 * 다이버젼스 신호 검색
 * @author macle
 */
public interface DivergenceSignalSearch {



    /**
     * 상승 신호 검색
     * 최근 발생한 다이버젼스중 가장 신뢰도 높은 기울기와 길이를 가진 다이버젼스를 돌려준다.
     * @param priceCandles 가격정보 캔들 배열
     * @param subIndexCandles 보조지표 캔들 배열
     * @param steadyRate 보합 비율 (백분율)
     * @param candleCount 캔들 건수 ( 몇개 기준으로 고가 , 저가인지등에 파악)
     * @return 다이버전스 정보
     */
    DivergenceSignal rise(Candle[] priceCandles, Candle [] subIndexCandles, double steadyRate, int candleCount);



    /**
     * 하락신호
     * 최근 발생한 다이버젼스중 가장 신뢰도 높은 기울기와 길이를 가진 다이버젼스를 돌려준다.
     * @param priceCandles 가격정보 캔들 배열
     * @param subIndexCandles 보조지표 캔들 배열
     * @param steadyRate 보합 비율 (백분율)
     * @param candleCount 캔들 건수 ( 몇개 기준으로 고가 , 저가인지등에 파악)
     * @return 다이버전스 정보
     */
    DivergenceSignal fall(Candle[] priceCandles, Candle[] subIndexCandles, double steadyRate, int candleCount);

}
