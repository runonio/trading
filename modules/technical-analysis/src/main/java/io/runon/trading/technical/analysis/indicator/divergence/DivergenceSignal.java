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

import io.runon.trading.PriceChangeType;

/**
 * 다이버젼스 신호
 * @author macle
 */
public class DivergenceSignal {

    //가겨변화유형
    //상승하락
    PriceChangeType priceChangeType;

    Divergence.Type type;

    
    //최대값 혹은 최저값을 구할때의 캔들 개수
    int candleCount;

    int index;

    int length;
    
    int gap;

    //원본 기울기 보통 가격이됨 
    double sourceGradient;
    
    //타켓 기울기 보통 보조지표가 됨
    double targetGradient;
    
    /**
     * 가격변화유형
     * @return 상승, 하락
     */
    public PriceChangeType getPriceChangeType() {
        return priceChangeType;
    }

    /**
     * 다이버젼스 유형
     * @return 일반, 히든, 과장된
     */
    public Divergence.Type getType() {
        return type;
    }

    /**
     * 최근 크로스 발생지점 얻기
     * @return 최근 크로스 발생지점
     */
    public int index() {
        return index;
    }

    /**
     * 다이버 젼스 길이  다이버젼스 시작점부터 의 길이
     * @return 다이버젼스 길이
     */
    public int length() {
        return length;
    }

    /**
     * 마지막 발생시점으로 부터의 거리 얻기
     * @return 마지막 발생시점으로 부터의 거리
     */
    public int gap() {
        return gap;
    }
}
