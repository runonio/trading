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

import io.runon.trading.Trade;
import io.runon.trading.TradingTimes;

import java.math.BigDecimal;


/**
 * trade 정보 추가용 (2번째 부터)
 * @author macle
 */
class NextTradeAdd implements TradeAdd {
    private final TradeCandles tradeCandles;

    /**
     * 생성자
     * @param tradeCandles tradeCandles
     */
    NextTradeAdd(TradeCandles tradeCandles){
        this.tradeCandles = tradeCandles;
    }

    @Override
    public TradeCandle addTrade(Trade trade){
        if(trade.getTime() < tradeCandles.lastCandle.getCloseTime()){
            //트레이드 정보 추가
            tradeCandles.lastCandle.addTrade(trade);
            return tradeCandles.lastCandle;
        }
        long timeGap = tradeCandles.getCandleTime();
        long nextStartTime =  tradeCandles.lastCandle.getCloseTime();
        long nextEndTime  = nextStartTime + timeGap;
        if(trade.getTime() < nextEndTime){
            //noinspection UnnecessaryLocalVariable
            TradeCandle tradeCandle =tradeCandles.addTradeNewCandle(trade, nextStartTime, nextEndTime);
            return tradeCandle;
        }

        BigDecimal lastPrice = tradeCandles.lastCandle.getClose();

        if(tradeCandles.isEmptyCandleContinue) {
            do {
                TradeCandle nextTradeCandle = new TradeCandle();

                nextTradeCandle.setOpen(lastPrice);
                nextTradeCandle.setClose(lastPrice);
                nextTradeCandle.setHigh(lastPrice);
                nextTradeCandle.setLow(lastPrice);
                nextTradeCandle.setOpenTime(nextStartTime);
                nextTradeCandle.setCloseTime(nextEndTime);

                tradeCandles.addCandle(nextTradeCandle);
                nextStartTime = nextEndTime;
                nextEndTime = nextStartTime + timeGap;
            } while (trade.getTime() >= nextEndTime);

            return tradeCandles.addTradeNewCandle(trade, nextStartTime , nextEndTime);
        }else{
            long startTime = TradingTimes.getOpenTime(timeGap, trade.getTime());
            return tradeCandles.addTradeNewCandle(trade, startTime , startTime + timeGap);
        }

    }
}