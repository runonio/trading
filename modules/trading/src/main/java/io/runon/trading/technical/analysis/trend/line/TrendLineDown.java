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
package io.runon.trading.technical.analysis.trend.line;

import io.runon.trading.technical.analysis.candle.TradeCandle;

/**
 * 하락추세선
 * @author macle
 */
public class TrendLineDown implements TrendLineCase {

    @Override
    public boolean isCountValid(TradeCandle tradeCandle) {
        //양봉이거나 같으면 유효하지 않음
        return tradeCandle.getClose().compareTo(tradeCandle.getOpen()) < 0;
    }
}
