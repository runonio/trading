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

package io.runon.trading.technical.analysis.candle.candles;

import io.runon.trading.CandleTimes;
import io.runon.trading.Trade;
import io.runon.trading.technical.analysis.candle.TradeAdd;
import io.runon.trading.technical.analysis.candle.TradeCandle;


/**
 * trade 정보 처음 추가용
 * @author macle
 */
class FirstTradeAdd implements TradeAdd {

    private final TradeCandles tradeCandles;

    /**
     * 생성자
     * @param tradeCandles TradeCandles
     */
    FirstTradeAdd(TradeCandles tradeCandles){
        this.tradeCandles = tradeCandles;
    }

    @Override
    public TradeCandle addTrade(Trade trade){
        if(tradeCandles.candleList.size() != 0){
            //이미 추가된 켄들이 있을경우
            tradeCandles.tradeAdd = new NextTradeAdd(tradeCandles);
            return tradeCandles.tradeAdd.addTrade(trade);
        }
        long timeGap = tradeCandles.getTimeGap();

        long startTime = CandleTimes.getOpenTime(timeGap, trade.getTime());
        TradeCandle tradeCandle =tradeCandles.addTradeNewCandle(trade, startTime, startTime + timeGap);
        tradeCandles.tradeAdd = new NextTradeAdd(tradeCandles);

        return tradeCandle;
    }
}