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
package io.runon.trading.technical.analysis.indicators.ichimoku;
/**
 * 일목균형 분석 정보
 * @author ccsweets
 */
public class IchimokuAnalysisData {
    /**
     * 일목균형 구름 유형
     */
    public enum IchimokuCloudType {POSITIVE,NEGATIVE}

    
    IchimokuCloudType cloudType;

    double cloudTopPrice;
    double cloudBottomPrice;

    /**
     * 기준시간
     */
    long time;
    /**
     * 구름크기
     */
    double cloudSize;

    /**
     * 구름의 평균 두께
     */
    double cloudAvgHeight;

    /**
     * 구름대 안에 가격이 머무르는지
     */
    boolean cloudPriceOverlap;

    /**
     * 구름대와 가격간의 거리차이
     */
    double cloudHeightDistance;


    /**
     * 구름대 돌파 이후 기간
     * 상향돌파 = 양수 , 하향돌파 = 음수
     */
    int cloudBreakThroughDay;

    /**
     * 구름대 돌파 이후 기간
     * 상향돌파 = 양수 , 하향돌파 = 음수
     * @return 돌파기간
     */
    public int getCloudBreakThroughDay() {
        return cloudBreakThroughDay;
    }

    /**
     * 구름대 돌파 이후 기간
     * 상향돌파 = 양수 , 하향돌파 = 음수
     * @param cloudBreakThroughDay 돌파기간
     */
    public void setCloudBreakThroughDay(int cloudBreakThroughDay) {
        this.cloudBreakThroughDay = cloudBreakThroughDay;
    }

    /**
     * 구름 크기
     * @return 구름 크기
     */
    public double getCloudSize() {
        return cloudSize;
    }

    /**
     * 구름 크기 설정
     * @param cloudSize 구름 크기
     */
    public void setCloudSize(double cloudSize) {
        this.cloudSize = cloudSize;
    }

    /**
     * 구름 평균 높이
     * @return 구름 평균 높이
     */
    public double getCloudAvgHeight() {
        return cloudAvgHeight;
    }

    /**
     * 구름 평균 높이 설정
     * @param cloudAvgHeight 구름 평균 높이
     */
    public void setCloudAvgHeight(double cloudAvgHeight) {
        this.cloudAvgHeight = cloudAvgHeight;
    }

    /**
     * 구름과 가격대가 겹쳤는지
     * @return 겹침여부
     */
    public boolean isCloudPriceOverlap() {
        return cloudPriceOverlap;
    }

    /**
     * 구름과 가격대가 겹쳤는지 설정
     * @param cloudPriceOverlap 겹침여부
     */
    public void setCloudPriceOverlap(boolean cloudPriceOverlap) {
        this.cloudPriceOverlap = cloudPriceOverlap;
    }

    /**
     * 구름 높이차
     * @return 구름 높이차
     */
    public double getCloudHeightDistance() {
        return cloudHeightDistance;
    }

    /**
     * 구름 높이차 설정
     * @param cloudHeightDistance Distance
     */
    public void setCloudHeightDistance(double cloudHeightDistance) {
        this.cloudHeightDistance = cloudHeightDistance;
    }

    /**
     * 시간을 얻는다
     * @return 시간 
     */
    public long getTime() {
        return time;
    }

    /**
     * 시간 설정
     * @param time 시간
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 구름 유형을 얻는다
     * @return 구름 유형
     */
    public IchimokuCloudType getCloudType() {
        return cloudType;
    }

    /**
     * 구름 유형 설정
     * @param cloudType 구름 유형
     */
    public void setCloudType(IchimokuCloudType cloudType) {
        this.cloudType = cloudType;
    }

    /**
     * 구름 최대 가격
     * @return 구름 최대 가격
     */
    public double getCloudTopPrice() {
        return cloudTopPrice;
    }

    /**
     * 구름 최대 가격 설정
     * @param cloudTopPrice 구름 최대 가격
     */
    public void setCloudTopPrice(double cloudTopPrice) {
        this.cloudTopPrice = cloudTopPrice;
    }

    /**
     * 구름 최소 가격
     * @return 구름 최소 가격
     */
    public double getCloudBottomPrice() {
        return cloudBottomPrice;
    }

    /**
     * 구름 최소 가격 설정
     * @param cloudBottomPrice 구름 최소 가격
     */
    public void setCloudBottomPrice(double cloudBottomPrice) {
        this.cloudBottomPrice = cloudBottomPrice;
    }
}
