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
package io.runon.trading.technical.analysis;

import io.runon.trading.PriceChangeRate;
import io.runon.trading.technical.analysis.candle.Candle;

import java.math.BigDecimal;

/**
 * candle 에서 사용하는 정보 일부를 BigDecimal 형으로 변환 시켜주는 유틸성 클래스
 * @author macle
 */
public class CandleBigDecimalChange {


    /**
     * 종가를 double 형 배열로 만들어서 돌려준다
     * @param candles 캔들 배열
     * @return 종가  배열
     */
    public static BigDecimal [] getCloseArray(Candle[] candles){
        BigDecimal [] array = new BigDecimal[candles.length];

        for (int i = 0; i <array.length ; i++) {
            array[i] = candles[i].getClose() ;
        }
        return array;

    }

    /**
     * 가격 변화율 배열로 변환
     * @param priceChangeRateArray 가격 변화율 배열
     * @return 가격 변화 배열
     */
    public static BigDecimal[] getChangeRateArray(PriceChangeRate[] priceChangeRateArray){
        BigDecimal [] array = new BigDecimal[priceChangeRateArray.length];

        for (int i = 0; i <array.length ; i++) {
            array[i] = priceChangeRateArray[i].getChangeRate() ;
        }
        return array;

    }

}
