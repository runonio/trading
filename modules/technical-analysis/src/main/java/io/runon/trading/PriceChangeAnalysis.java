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
package io.runon.trading;

/**
 * 매수, 매도 판단할때 사용함
 * 가격변화 분석 유형
 * @author macle
 */
public interface PriceChangeAnalysis {


    /**
     * 가격변화 예측유형
     * @return PriceChangeType 가격 변화 예층 유형( 상승, 하락, 보합)
     */
    PriceChangeType getPriceChangeType();


}
