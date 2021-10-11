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
package io.runon.trading.technical.analysis.subindex.ichimoku;

import com.seomse.commons.utils.time.DateUtil;
import io.runon.trading.technical.analysis.candle.CandleStick;

/**
 * 일목균형표의 분석 정보
 * @author ccsweets
 */
public class IchimokuBalanceAnalysis {

    /**
     * 선행스팬1과 선행스팬2를 이용한 분석정보 생성
     * @param candleStickArray 가격정보
     * @param ichimokuDataArray 일목데이터정보
     * @return data array
     */
    public static IchimokuAnalysisData[] getAnalysisDataArray(CandleStick[] candleStickArray,
                                                              IchimokuData [] ichimokuDataArray ) {

        /* 선행스팬 기준기간을 뺀만큼의 데이터 생성 */
        IchimokuAnalysisData[] resultArray = new IchimokuAnalysisData[candleStickArray.length];
        for (int i = candleStickArray.length-1; i >= 0 ; i--) {

            double closePrice = candleStickArray[i].getClose().doubleValue();
            String openStr = DateUtil.getDateYmd(candleStickArray[i].getOpenTime(),"yyyy-MM-dd");


            long time = candleStickArray[i].getOpenTime();

            boolean cloudStart = false;
            /* 구름크기 */
            double cloudSize=0.0;

            /* 구름의 평균 두께 */
            double cloudAvgHeight=0.0;

            /* 구름대 안에 가격이 머무르는지 */
            boolean cloudPriceOverlap=false;

            double leading1Price = ichimokuDataArray[i].getLeading1();
            double leading2Price = ichimokuDataArray[i].getLeading2();


            double cloudTopPrice = -1.0 , cloudBottomPrice = -1.0;

            if(leading1Price >= leading2Price){
                cloudTopPrice = leading1Price;
                cloudBottomPrice = leading2Price;
            } else {
                cloudTopPrice = leading2Price;
                cloudBottomPrice = leading1Price;
            }

            if(closePrice <= cloudTopPrice &&  closePrice >= cloudBottomPrice ) {
                cloudPriceOverlap = true;
            }



            /* 구름대와 가격간의 거리차이 */
            double cloudHeightDistance = 0.0;
            if(!cloudPriceOverlap) {
                if(cloudBottomPrice < closePrice ){
                    double diffPrice =  cloudBottomPrice - closePrice;
                    cloudHeightDistance =  diffPrice / closePrice * -1.0;
                } else {
                    double diffPrice =  closePrice -  cloudTopPrice;
                    cloudHeightDistance =  diffPrice / closePrice;
                }
            }

            //System.out.println("["+openStr+"] cloudTopPrice:"+cloudTopPrice+",cloudBottomPrice:"+cloudBottomPrice+",closePrice:"+closePrice+",cloudHeightDistance:"+cloudHeightDistance);

            IchimokuAnalysisData.IchimokuCloudType cloudType=null;

            int cloudCalcCount = 0;
            for (int j = i; j >= 0 ; j--) {

                cloudCalcCount++;
                double loopLeading1Price = ichimokuDataArray[j].getLeading1();
                double loopLeading2Price = ichimokuDataArray[j].getLeading2();

                if(loopLeading1Price == -1.0 || loopLeading2Price == -1.0){
                    continue;
                }
                double leadingPriceDistance = loopLeading1Price - loopLeading2Price;
                if(leadingPriceDistance < 0){
                    leadingPriceDistance *= -1;
                }

                double cloudHeight = leadingPriceDistance / (closePrice / 0.01);
                if(cloudHeight < 0){
                    cloudHeight*=-1;
                }

                /* 겹치는 구간은 스킵 */
                if(loopLeading1Price == loopLeading2Price){
                    continue;
                }
                /* 양운 */
                else if(loopLeading1Price > loopLeading2Price){
                    if(cloudStart && cloudType.equals(IchimokuAnalysisData.IchimokuCloudType.NEGATIVE) ){
                        break; // 꼬임 발생
                    } else {
                        cloudStart = true;
                        cloudType = IchimokuAnalysisData.IchimokuCloudType.POSITIVE;
                    }
                }
                /* 음운 */
                else {
                    if(cloudStart && cloudType.equals(IchimokuAnalysisData.IchimokuCloudType.POSITIVE) ){
                        break; // 꼬임 발생
                    } else {
                        cloudStart = true;
                        cloudType = IchimokuAnalysisData.IchimokuCloudType.NEGATIVE;
                    }
                }

                cloudSize += cloudHeight;
            }
            cloudAvgHeight = cloudSize / cloudCalcCount;
            IchimokuAnalysisData ichimokuAnalysisData = new IchimokuAnalysisData();
            ichimokuAnalysisData.setCloudSize(cloudSize);
            ichimokuAnalysisData.setCloudAvgHeight(cloudAvgHeight);
            ichimokuAnalysisData.setCloudType(cloudType);
            ichimokuAnalysisData.setTime(time);
            ichimokuAnalysisData.setCloudHeightDistance(cloudHeightDistance);
            ichimokuAnalysisData.setCloudPriceOverlap(cloudPriceOverlap);

            resultArray[i] = ichimokuAnalysisData;
        }

        for (int i = 1; i < resultArray.length; i++) {
            IchimokuAnalysisData ichimokuAnalysisData = resultArray[i];
            double cloudHeightDistance = ichimokuAnalysisData.getCloudHeightDistance();
            boolean upper = cloudHeightDistance >= 0.0;
            if(cloudHeightDistance == 0.0){
                ichimokuAnalysisData.setCloudBreakThroughDay(0);
                continue;
            }
            int dayCount = 0;
            for (int j = i-1; j > 0; j--) {
                IchimokuAnalysisData loopIchimokuAnalysisData = resultArray[j];
                double loopCloudHeightDistance = loopIchimokuAnalysisData.getCloudHeightDistance();
                if(loopCloudHeightDistance == 0.0){
                    break;
                }
                boolean loopUpper = loopCloudHeightDistance >= 0.0;
                if(upper == loopUpper){
                    dayCount++;
                } else {
                    // 돌파 발생
                    break;
                }
            }
            if(cloudHeightDistance < 0.0){
                dayCount *= -1.0;
            }
            ichimokuAnalysisData.setCloudBreakThroughDay(dayCount);
        }

        return resultArray;

    }
}
