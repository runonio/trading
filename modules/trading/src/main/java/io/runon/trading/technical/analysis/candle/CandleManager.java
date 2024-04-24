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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 캔들 관리자
 * @author macle
 */
public class CandleManager {

    private final Map<Long, TradeCandles> candleMap = new HashMap<>();

    private final TradeCandles[] tradeCandles;
    /**
     * 생성자
     * @param candleTimes long 캔들 생성을위한 캔들 생성 기준 타임 배열
     */
    public CandleManager( long [] candleTimes){

        TradeCandles[] tradeCandles = new TradeCandles[candleTimes.length];

        for (int i = 0; i <candleTimes.length ; i++) {
            tradeCandles[i] = new TradeCandles(candleTimes[i]);
            candleMap.put(candleTimes[i], tradeCandles[i]);
        }

        this.tradeCandles = tradeCandles;
    }

    /**
     * 저장개수 설정
     * @param saveCount int 저장개수
     */
    public void setSaveCount(int saveCount) {
        for (TradeCandles tradeCandle : tradeCandles) {
            tradeCandle.setCount(saveCount);
        }
    }


    /**
     * 거래정보 추가
     * @param trade Trade 거래 정보
     */
    public void addTrade(Trade trade){
        for(TradeCandles tradeCandles : tradeCandles){
            tradeCandles.addTrade(trade);
        }
    }

    /**
     * 캔들배열관리정보 얻기
     * @param time long 기준시간
     * @return TradeCandles 기준시간에 맞는 캔들배열관리정보
     */
    public TradeCandles getCandles(long time){
        return candleMap.get(time);
    }


    /**
     * 캔들 기준 시간 배열 얻기
     * @return long [] 캔들 기준 시간 배열
     */
    public long [] getTimes(){
        long [] times = new long[candleMap.size()];
        Set<Long> keys = candleMap.keySet();
        int index = 0;
        for(Long time : keys){
            times[index] = time;
            index ++;
        }
        Arrays.sort(times);
        return times;
    }


}
