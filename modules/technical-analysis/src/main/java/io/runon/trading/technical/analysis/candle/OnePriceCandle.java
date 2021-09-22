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

package io.runon.trading.technical.analysis.candle;

import java.math.BigDecimal;

/**
 * 단일가 캔들
 * 다이버전스에 활용됨
 * @author macle
 */
public class OnePriceCandle implements Candle{

    private final BigDecimal price;

    /**
     * 생성자
     * @param price 단일가
     */
    public OnePriceCandle(BigDecimal price){
        this.price = price;    
    }
    
    @Override
    public BigDecimal getOpen() {
        return price;
    }

    @Override
    public BigDecimal getClose() {
        return price;
    }

    @Override
    public BigDecimal getHigh() {
        return price;
    }

    @Override
    public BigDecimal getLow() {
        return price;
    }
}
