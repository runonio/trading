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
package io.runon.trading.example;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.technical.analysis.candle.CandleManager;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.candles.TradeCandles;
import io.runon.trading.technical.analysis.pattern.CandlePatternPoint;
import io.runon.trading.technical.analysis.pattern.lower.shadow.HammerPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 패턴 시뮬레이터
 * @author macle
 */
public class PatternSimulator {

    private static final Logger logger = LoggerFactory.getLogger(PatternSimulator.class);

    public static void main(String[] args) {


        CandleManager candleManager = CandleManagerExample.makeCandleManager();

        candleManager.setSaveCount(5000);

        long [] times = candleManager.getTimes();

        //정렬된 데이터라 순서정보가 명확하

        for(long time : times){


            TradeCandles tradeCandles = candleManager.getCandles(time);

            if(time < Times.HOUR_4) {

                //0.2% 보합
                tradeCandles.setSteadyGapRatio(new BigDecimal("0.002"));
                //0.5% 짧은 캔들 기준은 0.5%
                tradeCandles.setShortGapRatio(new BigDecimal("0.005"));
            }else if(time < Times.HOUR_12) {

                //0.2% 보합
                tradeCandles.setSteadyGapRatio(new BigDecimal("0.002"));
                //1.5% 짧은 캔들 기준은 1.5%
                tradeCandles.setShortGapRatio(new BigDecimal("0.015"));
            }else{

                //0.5% 보합
                tradeCandles.setSteadyGapRatio(new BigDecimal("0.005"));
                //3% 짧은 캔들 기준은 0.3%
                tradeCandles.setShortGapRatio(new BigDecimal("0.003"));
            }
            tradeCandles.setCandleType();

            HammerPattern hammerPattern = new HammerPattern();
            hammerPattern.setCandles(tradeCandles);
            CandlePatternPoint [] points = hammerPattern.getPoints();

            if(points.length == 0){
                logger.debug("candle time : " + time +", pattern point null");
                continue;
            }

            TradeCandle [] candles = tradeCandles.getCandles();
            logger.debug("candle time : " + time  + ", pattern count: " + points.length);

            int pattenCount = 0;
            int upCount = 0;

            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < points.length ; i++) {
                CandlePatternPoint candlePatternPoint = points[i];
                TradeCandle patternCandle = candlePatternPoint.getCandle();
                int candleIndex = -1;

                for (int index = 0; index <candles.length ; index++) {
                    if(patternCandle == candles[index]){
                        candleIndex = index;
                        break;
                    }
                }

                if(candleIndex == -1){
                    logger.error("candle index not search");
                    continue;
                }

                if(candleIndex == points.length-1){
                    continue;
                }


                TradeCandle nextCandle =  candles[candleIndex+1];
                pattenCount++;
                if(patternCandle.getClose().compareTo( nextCandle.getClose()) < 0 ){
                    upCount++;

                    logger.info("패턴 성공: " + patternCandle.getCloseTime() + ", " + patternCandle.getClose() + ", " + nextCandle.getClose());


                }else{
                    logger.info("패턴 실패: " + patternCandle.getCloseTime() + ", " + patternCandle.getClose() + ", " + nextCandle.getClose());
                }

            }


            logger.info("pattenCount " + pattenCount + "upCount " + upCount + " percent " + (double)upCount/(double)pattenCount);

        }

    }

}
