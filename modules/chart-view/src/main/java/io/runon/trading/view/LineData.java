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
package io.runon.trading.view;

import io.runon.trading.PriceOpenTime;

import java.math.BigDecimal;

/**
 * 라인 정보
 * @author ccsweets
 */
public class LineData implements PriceOpenTime {
    BigDecimal price;
    long time;

    public LineData(BigDecimal price, long time){
        this.price = price;
        this.time = time;
    }

    @Override
    public long getOpenTime() {
        return time;
    }

    @Override
    public BigDecimal getClose() {
        return price;
    }
}
