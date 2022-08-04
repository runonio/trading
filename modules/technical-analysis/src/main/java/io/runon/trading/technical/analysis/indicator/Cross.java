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

package io.runon.trading.technical.analysis.indicator;

import io.runon.trading.BigDecimals;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 골든 크로스 데드 크로스 잡기
 * @author macle
 */
public class Cross {

    public static final int MIN_COUNT = 15;

    public static final Cross NONE = none();
    private static Cross none(){
        Cross none = new Cross();
        none.type = Type.NONE;
        return none;
    }

    public enum Type{
        NONE
        , GOLDEN // 골든크로스
        , DEAD // 데드크로스
    }

    Type type;
    int index;
    BigDecimal disparity;

    /**
     * 크로스 유형얻기
     * @return 골드크로스 or 데드 크로스
     */
    public Type getType() {
        return type;
    }


    /**
     * 실제 크로스가 발생한 위치 (겹친위치)
     * 이격도가 넘어선 시점이 유효하기 때문에 실위치랑 다르다
     * @return 실제 크로스가 발생한 위치
     */
    public int getIndex() {
        return index;
    }

    /**
     * 이격도
     * @return 이격도
     */
    public BigDecimal getDisparity() {
        return disparity;
    }


    public static Cross getCross(BigDecimal[] shotArray, BigDecimal [] longArray, BigDecimal disparity ){
        return getCross(shotArray, longArray, disparity, MIN_COUNT);
    }

    /**
     * 크로스 지점 얻기
     * short 5 이면 long 은 10 15 20 등.
     * 5일 이평선과 20일 이평선의 경우를 생각하면 됨
     * 두 배열의 크기는 반드시 일치 할것
     * @param shotArray 짧은 배열
     * @param longArray 긴배열
     * @param disparity 돌파를 확인하는 이격도 겹치는 지점에서 이격도이상에서 보이는자리에서 한다.  short/long *100 골든크로스를 얻으려면 100보다 큰값, 데드크로스를 얻으로면 100보다 작은값
     * @param minCount 크로스 발생이 유효하다고 판단되는 이전 캔들의 건수
     * @return 크로스 발생 유형과 위치
     */
    public static Cross getCross(BigDecimal[] shotArray, BigDecimal [] longArray, BigDecimal disparity, int minCount ){

        int lastIndex = longArray.length -1;

        if( disparity == null || disparity.compareTo(BigDecimal.ZERO) < 0 ){
            throw new IllegalArgumentException("disparity more than 0: " + disparity);
        }

        if( shotArray.length < minCount ){
            throw new IllegalArgumentException("array length more than min count array: " + shotArray.length + ", min count:" + minCount);
        }

        if( shotArray.length != longArray.length ){
            throw new IllegalArgumentException("shotArray.length == longArray.length short length: " + shotArray.length + ", long length: " + longArray.length);
        }

        if( shotArray[lastIndex].compareTo( longArray[lastIndex]) > 0 && disparity.compareTo(BigDecimals.DECIMAL_100) >= 0){
            //골든 크로스 체크
            int crossIndex = getCrossIndex(longArray, shotArray, minCount);
            if(crossIndex == -1){
                return NONE;
            }

            int index = shotArray.length-1;
            BigDecimal lastDisparity = shotArray[index].divide(longArray[index], 10, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100);
            if(lastDisparity.compareTo(disparity) < 0){
                return NONE;
            }
            if(isBetween(shotArray,longArray, crossIndex, shotArray.length-1, disparity ,true)){
                return NONE;
            }
            Cross cross = new Cross();
            cross.type = Type.GOLDEN;
            cross.index = crossIndex;
            cross.disparity = lastDisparity;
            return cross;
        }


        if( shotArray[lastIndex].compareTo( longArray[lastIndex]) < 0 && disparity.compareTo(BigDecimals.DECIMAL_100) <= 0){
            //데드 크로스 체크
            int crossIndex = getCrossIndex(shotArray, longArray, minCount);
            if(crossIndex == -1){
                return NONE;
            }

            int index = shotArray.length-1;
            BigDecimal lastDisparity = shotArray[index].divide(longArray[index], 10, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100);
            if(lastDisparity.compareTo(disparity) > 0){
                return NONE;
            }

            if(isBetween(shotArray,longArray, crossIndex, shotArray.length-1, disparity ,false)){
                return NONE;
            }

            Cross cross = new Cross();
            cross.type = Type.DEAD;
            cross.index = crossIndex;
            cross.disparity = lastDisparity;
            return cross;
        }

        return NONE;

    }

    /**
     * 사이에 다른 유효한 지점이 없었는지 여부
     */
    public static boolean isBetween(BigDecimal [] shotArray, BigDecimal [] longArray, int start, int end, BigDecimal disparity, boolean isLong){
        for (int i = start; i < end ; i++) {
            if(isLong && shotArray[i].divide(longArray[i], 10, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).compareTo(disparity) >= 0){
                return true;
            }

            if(!isLong && shotArray[i].divide(longArray[i], 10, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_100).compareTo(disparity) <= 0){
                return true;
            }
        }

        return false;
    }

    /**
     * 큰값이 작은값보다 작아야함
     * @param small 작은값
     * @param large 큰값
     * @return 이전에 발생한 지점부터의 gap, 유효하지 않을경우 -1
     */
    private static int getCrossIndex(BigDecimal [] small, BigDecimal [] large, int minCount){

        int min = minCount-1;

        int index = -1;
        for (int i = small.length - 2 ; i > min ; i--) {
            if(large[i].compareTo(small[i]) < 0){
                index = i;
                break;
            }
        }

        if(index == -1){
            return -1;
        }

        int count = 0;

        for (int i = index; i > -1 ; i--) {
            if(large[i].compareTo(small[i]) > 0){
                break;
            }

            if(large[i].compareTo(small[i]) <= 0){
                count++;
            }

            if(count >= minCount){
                return index +1;
            }
        }

        if(count < minCount){
            return -1;
        }


        return index + 1;
    }

}
