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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * 다이버전스 뜻은 사전적 의미는 차이, 이격, 발산 등을 의미합니다. 대부분의 지표는 가격을 따라가기 때문에 가격이 오르면 같이 오르고 내리면 같이 내립니다. 그러나 종종 반대로 움직이거나 하는 경우가
 * 있는데 이러한 주가와 지표의 움직임의 이격, 차이, 발산을 다이버전스라고 정의합니다. 다이버전스는 여러가지 종류를 정의할 수 있겠으나 일반적으로 많이 나타나며 신뢰할만한 다이버전스는 세종류이며 사실 이중
 * 한 종류가 다른 한 종류에 포함 된다고 할 수도 있기에 크게는 일반 다이버전스와 히든 다이버전스 두 종류로 기억하셔도 됩니다. 다른 여러가지를 정의할 수 있겠으나 실전에서 잘 나타나지 않거나 신뢰도가 높지
 * 않기에 여기서는 세가지만 소개하도록 하겠습니다.
 *
 * 각각의 다이버전스는 상승 다이버전스와 하락 다이버전스으로 나뉘며 상승 다이버전스는 향후 상승 추세로의 전환, 하락 다이버전스는 향후 하락 추세로의 전환이 될 수도 있음을 암시합니다.
 *
 * (1) 일반 다이버전스(Regular Divergence)
 * 일반 다이버전스
 * [그림 7-1] 일반 다이버전스
 * 일반 하락 다이버전스는 시세의 고점이 상승하나 지표는 이를 따라가지 못하여 고점이 하락하는 경우를 일반 하락다이버전스가 발생했다고 하며 추세 지표를 예를 들자면 가격의 고점을 갱신하였으나 추세는 고점을
 * 갱신하지 못함을 의미합니다. 이는 가격은 상승하였으나 상승 추세는 둔화되었음을 의미하기에 조만간 추세가 반전될 수도 있음을 암시합니다. 일반 상승 다이버전스는 시세의 저점이 하락하나 지표의 저점은 높아지는
 * 경우로 하락 추세가 둔화되기에 조만간 추세가 반전될 수도 있음을 암시합니다. 거래량 지표에서 발생한다면 시세의 방향에 비해 매수세, 혹은 매도세가 약해짐을 의미하기에 추세가 반전될 수도 있음을 의미하며
 * 모멘텀 지표에서 발생한다면 모멘텀이 둔화되어 반전될 수도 있음을 의미합니다.
 *
 * (2) 과장된 다이버전스(Exaggerated Divergence)
 * 과장 다이버전스
 * [그림 7-2] 과장된 다이버전스
 * 과장된 다이버전스는 일반 다이버전스와 유사하며 개인적으로는 일반 다이버전스의 일종으로 봐도 된다 생각합니다. 일반 다이버전스에서 시세의 고점이나 저점이 보합을 이룬다고 하더라도 일반 다이버전스와 본질적으로
 * 큰 차이가 없기에 비슷한 효과를 낼 것으로 생각합니다. 하지만 일반 다이버전스보다 신뢰도가 약한 것은 사실입니다.
 *
 * (3) 히든 다이버전스(Hidden Divergence)
 * 히든 다이버전스
 * [그림 7-3] 히든 다이버전스
 * 히든 다이버전스는 주로 일반다이버전스가 나타난 후에 나타나는 경우가 많습니다. 이런 경우 히든 다이버전스를 우선시 한다고 알려져 있습니다. 예를 들자면 하락장에서 쌍바닥을 만들고 상승다이버전스가
 * 생성되었다면 상승 반전을 기대할 수 있으나 이후 고점에서 히든 하락 다이버전스가 생성되는 경우가 있습니다. 이것은 지표는 끌어 올리고 있으나 가격 고점이 내려 오는 것으로 상승다이버전스의 효과는 이미
 * 고점을 만듦으로써 종료되고 고점이 내려오는 하락추세를 만드는 것이기에 하락추세의 지속을 의미합니다. 즉, 히든 다이버전스는 주로 일반 다이버전스 이후에 나타나며 이때 일반 다이버전스의 효과가 종료됨을
 * 의미하기에 기존의 추세가 지속됨을 의미합니다.
 *
 * @author ccsweets
 */
public class Divergence {
    /**
     * 다이버전스 Array를 돌려준다. 캔들
     * @param candleArr 캔들 배열
     * @param compareArr 지표 배열
     * @param minLength 최소 탐색 길이
     * @param maxLength 최대 탐색 길이
     * @param size 최대 배열 개수
     * @return
     */
    public static DivergenceData[] getArray(Candle[] candleArr, BigDecimal[] compareArr, int minLength, int maxLength, int size){

        Candle[] newPriceArr = Arrays.copyOfRange(candleArr, candleArr.length - size,  candleArr.length);
        BigDecimal[] newCompareArr = Arrays.copyOfRange(compareArr, compareArr.length - size, compareArr.length);

        return getArray(newPriceArr, newCompareArr, minLength, maxLength);
    }

    /**
     * 다이버전스 Array를 돌려준다. 캔들
     * @param candleArr 캔들 배열
     * @param compareArr 지표 배열
     * @param minLength 최소 탐색 길이
     * @param maxLength 최대 탐색 길이
     * @return
     */
    public static DivergenceData[] getArray(Candle[] candleArr, BigDecimal[] compareArr, int minLength, int maxLength){

        int lastBearIndex = candleArr.length;
        int lastBullIndex = candleArr.length;
        DivergenceData[] divergenceDataArr = new DivergenceData[candleArr.length];

        for (int i = 0; i < candleArr.length; i++) {
            divergenceDataArr[i] = new DivergenceData();
        }
        BigDecimal[] highArr = new BigDecimal[candleArr.length];
        BigDecimal[] lowArr = new BigDecimal[candleArr.length];
        for (int i = 0; i < candleArr.length; i++) {
            Candle candle = candleArr[i];
            BigDecimal high = candle.getHigh();
            BigDecimal low = candle.getLow();

            highArr[i] = high;
            lowArr[i] = low;

        }

        // find normal bear
        for (int i = highArr.length -1 ; i-maxLength >= 0  ; i--) {
            // 이미 찾은 구간
            if(i > lastBearIndex){
                continue;
            }
            BigDecimal price = highArr[i];
            BigDecimal prevPrice = highArr[i-1];
            BigDecimal compare = compareArr[i];

            // 이전 값이 같거나 크면 스킵
            if(prevPrice.compareTo(price) == 0 || prevPrice.compareTo(price) > 0){
                continue;
            }

            BigDecimal[] prevCompareArr = Arrays.copyOfRange(compareArr, i - maxLength, i);
            BigDecimal[] prevPriceArr = Arrays.copyOfRange(highArr, i - maxLength, i);

            int maxIndex = getMaxIndex(prevCompareArr);
            int maxI = i - maxLength + maxIndex;

            BigDecimal maxCompare = prevCompareArr[maxIndex];
            if(maxCompare.equals(new BigDecimal("0"))){
                continue;
            }
            BigDecimal maxPrice = prevPriceArr[maxIndex];

            // 현재 데이터가 최대값 보다 높으면 스킵
            if(compare.compareTo(maxCompare) >= 0 || price.compareTo(maxPrice) <= 0){
                continue;
            }

            BigDecimal[] prevBearCompareArr = Arrays.copyOfRange(prevCompareArr, maxIndex+1, prevCompareArr.length);
            BigDecimal[] prevBearPriceArr = Arrays.copyOfRange(prevPriceArr, maxIndex+1, prevCompareArr.length);

            //최소단위 비교
            if(prevBearCompareArr.length + 2 < minLength){
                continue;
            }

            boolean allCompareIsDown = checkLineOver(prevBearCompareArr, maxCompare, compare, true);
            boolean allPriceIsDown = checkLineOver(prevBearPriceArr, maxPrice, price, true);

            // 모든 값이 선 아래 있지 않음
            if(!allCompareIsDown || !allPriceIsDown){
                continue;
            }

            // 비교 지표의 Bearish 찾기 완료

            prevPriceArr = Arrays.copyOfRange(highArr, maxI, i);
            BigDecimal prevArrPriceMax = getMax(prevPriceArr);

            // 현재 가격이 가장 높지 않으면 스킵
            if(price.compareTo(prevArrPriceMax) < 0){
                continue;
            }

            // 탐색 완료
            DivergenceData divergenceData = new DivergenceData();
            divergenceData.setDivergenceType(DivergenceType.REGULAR);
            divergenceData.setDivergenceUpDownType(DivergenceUpDownType.BEARISH);
            divergenceData.setDivergenceLength(i - maxI);
            lastBearIndex = maxI;

            BigDecimal priceChange = price.subtract(maxPrice).divide(maxPrice, 3, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            BigDecimal compareChange = compare.subtract(maxCompare).divide(maxCompare, 3, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            divergenceData.setPriceChange(priceChange);
            divergenceData.setCompareChange(compareChange);

            divergenceDataArr[i] = divergenceData;
        }

        // find normal bull
        for (int i = lowArr.length -1 ; i-maxLength >= 0  ; i--) {
            // 이미 찾은 구간
            if(i > lastBullIndex){
                continue;
            }
            BigDecimal price = lowArr[i];
            BigDecimal prevPrice = lowArr[i-1];
            BigDecimal compare = compareArr[i];


            // 이전 값이 같거나 작으면 스킵
            if(prevPrice.compareTo(price) == 0 || prevPrice.compareTo(price) < 0){
                continue;
            }


            BigDecimal[] prevCompareArr = Arrays.copyOfRange(compareArr, i - maxLength, i);
            BigDecimal[] prevPriceArr = Arrays.copyOfRange(lowArr, i - maxLength, i);

            int minIndex = getMinIndex(prevCompareArr);
            int minI = i - maxLength + minIndex;

            BigDecimal minCompare = prevCompareArr[minIndex];
            if(minCompare.equals(new BigDecimal("0"))){
                continue;
            }
            BigDecimal minPrice = prevPriceArr[minIndex];

            // 현재 데이터가 최소값 보다 낮으면 스킵
            if(compare.compareTo(minCompare) <= 0  || price.compareTo(minPrice) >= 0){
                continue;
            }

            BigDecimal[] prevBullCompareArr = Arrays.copyOfRange(prevCompareArr, minIndex+1, prevCompareArr.length);
            BigDecimal[] prevBullPriceArr = Arrays.copyOfRange(prevPriceArr, minIndex+1, prevCompareArr.length);
            //최소단위 비교
            if(prevBullCompareArr.length + 2 < minLength){
                continue;
            }

            boolean allCompareIsUp = checkLineOver(prevBullCompareArr, minCompare, compare, false);
            boolean allPriceIsUp = checkLineOver(prevBullPriceArr, minPrice, price, false);

            // 모든 값이 선 위에 있지 않음
            if(!allCompareIsUp || !allPriceIsUp){
                continue;
            }

            // 비교 지표의 Bullish 찾기 완료
            prevPriceArr = Arrays.copyOfRange(highArr, minI, i);
            BigDecimal prevArrPriceMin = getMin(prevPriceArr);

            // 현재 가격이 가장 낮지 않으면 스킵
            if(price.compareTo(prevArrPriceMin) > 0){
                continue;
            }

            // 탐색 완료
            DivergenceData divergenceData = divergenceDataArr[i];
            divergenceData.setDivergenceType(DivergenceType.REGULAR);
            divergenceData.setDivergenceUpDownType(DivergenceUpDownType.BULLISH);
            divergenceData.setDivergenceLength(i - minI);
            lastBullIndex = minI;
            BigDecimal priceChange = price.subtract(minPrice).divide( minPrice, 3, RoundingMode.HALF_UP ).multiply(new BigDecimal("100"));
            BigDecimal compareChange = compare.subtract(minCompare).divide( minCompare, 3, RoundingMode.HALF_UP ).multiply(new BigDecimal("100"));
            divergenceData.setPriceChange(priceChange);
            divergenceData.setCompareChange(compareChange);

            divergenceDataArr[i] = divergenceData;
        }

        lastBearIndex = candleArr.length;
        lastBullIndex = candleArr.length;

        // find hidden bear
        for (int i = highArr.length -1 ; i-maxLength >= 0  ; i--) {
            // 이미 찾은 구간
            if(i > lastBearIndex){
                continue;
            }
            BigDecimal price = highArr[i];
            BigDecimal prevPrice = highArr[i-1];
            BigDecimal compare = compareArr[i];

            // 이전 값이 같거나 크면 스킵
            if(prevPrice.compareTo(price) == 0 || prevPrice.compareTo(price) > 0){
                continue;
            }

            BigDecimal[] prevCompareArr = Arrays.copyOfRange(compareArr, i - maxLength, i);
            BigDecimal[] prevPriceArr = Arrays.copyOfRange(highArr, i - maxLength, i);

            int minIndex = getMinIndex(prevCompareArr);
            int minI = i - maxLength + minIndex;

            BigDecimal minCompare = prevCompareArr[minIndex];
            if(minCompare.equals(new BigDecimal("0"))){
                continue;
            }
            BigDecimal minPrice = prevPriceArr[minIndex];

            // 현재 데이터가 최소값 보다 낮으면 스킵
            if(compare.compareTo(minCompare) <= 0 || price.compareTo(minPrice) >= 0){
                continue;
            }

            BigDecimal[] prevBearCompareArr = Arrays.copyOfRange(prevCompareArr, minIndex+1, prevCompareArr.length);
            BigDecimal[] prevBearPriceArr = Arrays.copyOfRange(prevPriceArr, minIndex+1, prevCompareArr.length);
            //최소단위 비교
            if(prevBearCompareArr.length + 2 < minLength){
                continue;
            }

            boolean allCompareIsDown = checkLineOver(prevBearCompareArr, minCompare, compare, true);
            boolean allPriceIsDown = checkLineOver(prevBearPriceArr, minPrice, price, true);

            // 모든 값이 선 위에 있지 않음
            if(!allCompareIsDown || !allPriceIsDown){
                continue;
            }

            // 탐색 완료
            DivergenceData divergenceData = divergenceDataArr[i];
            divergenceData.setDivergenceType(DivergenceType.HIDDEN);
            divergenceData.setDivergenceUpDownType(DivergenceUpDownType.BEARISH);
            divergenceData.setDivergenceLength(i - minI);
            lastBearIndex = minI;

            BigDecimal priceChange = price.subtract(minPrice).divide( minPrice, 3, RoundingMode.HALF_UP ).multiply(new BigDecimal("100"));
            BigDecimal compareChange = compare.subtract(minCompare).divide( minCompare, 3, RoundingMode.HALF_UP ).multiply(new BigDecimal("100"));
            divergenceData.setPriceChange(priceChange);
            divergenceData.setCompareChange(compareChange);

            divergenceDataArr[i] = divergenceData;
        }

        // find hidden bull
        for (int i = lowArr.length -1 ; i-maxLength >= 0  ; i--) {
            // 이미 찾은 구간
            if(i > lastBullIndex){
                continue;
            }
            BigDecimal price = lowArr[i];
            BigDecimal prevPrice = lowArr[i-1];
            BigDecimal compare = compareArr[i];


            // 이전 값이 같거나 크면 스킵
            if(prevPrice.compareTo(price) == 0 || prevPrice.compareTo(price) < 0){
                continue;
            }


            BigDecimal[] prevCompareArr = Arrays.copyOfRange(compareArr, i - maxLength, i);
            BigDecimal[] prevPriceArr = Arrays.copyOfRange(lowArr, i - maxLength, i);

            int maxIndex = getMaxIndex(prevCompareArr);
            int maxI = i - maxLength + maxIndex;

            BigDecimal maxCompare = prevCompareArr[maxIndex];
            if(maxCompare.equals(new BigDecimal("0"))){
                continue;
            }
            BigDecimal maxPrice = prevPriceArr[maxIndex];

            // 현재 데이터가 최대값 보다 높으면 스킵
            if(compare.compareTo(maxCompare) >= 0 || price.compareTo(maxPrice) <= 0){
                continue;
            }

            BigDecimal[] prevBearCompareArr = Arrays.copyOfRange(prevCompareArr, maxIndex+1, prevCompareArr.length);
            BigDecimal[] prevBearPriceArr = Arrays.copyOfRange(prevPriceArr, maxIndex+1, prevCompareArr.length);

            //최소단위 비교
            if(prevBearCompareArr.length + 2 < minLength){
                continue;
            }

            boolean allCompareIsUp = checkLineOver(prevBearCompareArr, maxCompare, compare, false);
            boolean allPriceIsUp = checkLineOver(prevBearPriceArr, maxPrice, price, false);

            // 모든 값이 선 위에 있지 않음
            if(!allCompareIsUp || !allPriceIsUp){
                continue;
            }

            // 비교 지표의 Bearish 찾기 완료

            // 탐색 완료
            DivergenceData divergenceData = new DivergenceData();
            divergenceData.setDivergenceType(DivergenceType.HIDDEN);
            divergenceData.setDivergenceUpDownType(DivergenceUpDownType.BULLISH);
            divergenceData.setDivergenceLength(i - maxI);
            lastBullIndex = maxI;

            BigDecimal priceChange = price.subtract(maxPrice).divide( maxPrice, 3, RoundingMode.HALF_UP ).multiply(new BigDecimal("100"));
            BigDecimal compareChange = compare.subtract(maxCompare).divide( maxCompare, 3, RoundingMode.HALF_UP ).multiply(new BigDecimal("100"));
            divergenceData.setPriceChange(priceChange);
            divergenceData.setCompareChange(compareChange);

            divergenceDataArr[i] = divergenceData;
        }


        return divergenceDataArr;
    }

    /**
     * 다이버전스 선 체크를 위한 값 체크
     * @param checkArr
     * @param start
     * @param end
     * @param isBear
     * @return
     */
    private static boolean checkLineOver(BigDecimal[] checkArr, BigDecimal start, BigDecimal end, boolean isBear) {

        BigDecimal lineAverage = start.subtract(end).divide(new BigDecimal(checkArr.length + 1), 5, RoundingMode.HALF_UP);

        for (int i = 0; i < checkArr.length; i++) {
            BigDecimal check = checkArr[i];

            // check + (lineAverage * i)
            BigDecimal checkPlusAverage = check.add(lineAverage.multiply(new BigDecimal(i+1)));
            if( isBear && checkPlusAverage.compareTo(start) > 0 ){
                return false;
            } else if( !isBear && checkPlusAverage.compareTo(start) < 0){
                return false;
            }
        }

        return true;

    }

    /**
     * 배열의 최소값을 구한다
     * @param priceArr
     * @return
     */
    private static BigDecimal getMin(BigDecimal[] priceArr){
        BigDecimal minPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        for (BigDecimal price : priceArr) {
            if(price.compareTo(minPrice) < 0){
                minPrice = price;
            }
        }
        return minPrice;
    }

    /**
     * 배열의 최대값을 구한다
     * @param priceArr
     * @return
     */
    private static BigDecimal getMax(BigDecimal[] priceArr){
        BigDecimal maxPrice = new BigDecimal("0.0");
        for (BigDecimal price : priceArr) {
            if(price.compareTo(maxPrice) > 0){
                maxPrice = price;
            }
        }
        return maxPrice;
    }

    /**
     * 배열의 최대값의 인덱스를 구한다
     * @param priceArr
     * @return
     */
    private static int getMaxIndex(BigDecimal[] priceArr){
        int max = 0;
        BigDecimal maxPrice = new BigDecimal("0.0");
        for (int i = priceArr.length-1; i >= 0 ; i--) {
            BigDecimal price = priceArr[i];
            if(price.compareTo(maxPrice) > 0){
                maxPrice = price;
                max = i;
            }
        }
        return max;
    }

    /**
     * 배열의 최소값의 인덱스를 구한다
     * @param priceArr
     * @return
     */
    private static int getMinIndex(BigDecimal[] priceArr){
        int min = 0;
        BigDecimal minPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        for (int i = priceArr.length-1; i >= 0 ; i--) {
            BigDecimal price = priceArr[i];
            if(price.compareTo(minPrice) < 0){
                minPrice = price;
                min = i;
            }
        }
        return min;
    }

}
