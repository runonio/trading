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

import io.runon.trading.Candle;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.일반 다이버전스
 * - 일반하락 다이버전스
 *  주가의 고점을 상승하고 있으나, 보조지표 고점은 하락하고 있는상태
 *  주가 고점이 상승하고 있으나, 보조지표 고점은 횡보하고 있는 상태도 하락 다이버전스임
 *  주가 고점이 횡보하고 있고, 보조지표 고점이 하락하고 있는 상태도 하락 다이버전스임.
 *
 * - 일반상승 다이버전스
 *  주가의 저점이 하락하고 있고, 보조지표 저점이 상승하고 있는 상태
 *  주가의 저점이 하락하고 있으니, 보조지표 저점은 횡보하고 있는 상태도 상승 다이버전스임.
 *  주가의 저점이 횡보하고 있고, 보조지표 저점이 상승하고 있는 상태도 상승 다이버전스임
 *  상승추세로 전환 가능성 높음
 *

 * @author macle
 */
public class RegularDivergence implements DivergenceSignalSearch {


    @Override
    public DivergenceSignal rise(Candle[] priceCandles, Candle [] subIndexCandles, double steadyRate, int candleCount) {


        // 최근에 주가의 저점이 하락하고 있는 구간 찾기
        //
        // 기간대비 하락률로 해야하나..
        // 기울기와 두점사이의 거리 활용하기
        // 기울기
        // https://terms.naver.com/entry.nhn?docId=3350308&cid=60210&categoryId=60210


        int [] priceLowIndexes = getLowIndexes(priceCandles, candleCount);
        int [] subLowIndexes = getLowIndexes( subIndexCandles, candleCount);
        
        
        // 주가의 저점이 하락하고 있고 보조지표 저점이 상승하고 있는 상태
        // 보조지표의 저점이 호이보하고 있는 상태도 상승 다이버젼스
        //위에 내용 구현하기
        int end = priceLowIndexes.length -1;
        for (int i = priceLowIndexes.length -1; i >0 ; i--) {
            //주가의 저점이 하락하고 있는구간
            if(priceCandles[priceLowIndexes[i-1]].getLow().doubleValue() < priceCandles[priceLowIndexes[i]].getLow().doubleValue() ){
                // 주가의 저점이 상승 했다면 하락 구간이 아님
               
                int start = i;
                if(start == end){
                    //구간이 존재하지 않으면
                    continue;
                }
                
                //보조지표 저짐이 횡보하거나 상승하는 구간 찾기 
               

            }

        }


        return null;
    }

    private DivergenceSignal lowUpCheck(int [] priceLowIndexes, int [] subLowIndexes, int startIndex, int end){



        return null;

    }


    private int [] getLowIndexes(Candle[] candles, int candleCount){
        List<Integer> indexList = new ArrayList<>();
        outer:
        for (int i = 1; i <candles.length; i++) {
            //저점이 낮아지는지 체크한다

            int start = i-candleCount;
            if(start < 0){
                start = 0;
            }

            double low = candles[i].getLow().doubleValue() ;


            for (int j = start; j < i ; j++) {
                if(low > candles[i].getLow().doubleValue() ){
                    continue outer;
                }

            }

            indexList.add(i);
        }

        int [] indexes = new int[indexList.size()];

        for (int i = 0; i <indexes.length ; i++) {
            indexes[i] = indexList.get(i);
        }

        return indexes;

    }

    //보조지표 저점 횡보 및 상승구간 찾기
    private DivergenceSignal riseSubIndexLowUp(double[] subIndexArray, List<Integer> indexList, double steadyRate, int candleCount){
        
        //전체 구간의 상승률
        //기간내 보조지표 저점 상승기간이 있는지
        for (int i = subIndexArray.length -2; i > -1 ; i--) {
            
        }
        






        indexList.clear();
        return null;
    }

 

    @Override
    public DivergenceSignal fall(Candle[] priceCandles, Candle [] subIndexCandles, double steadyRate, int candleCount) {
        return null;
    }
}
