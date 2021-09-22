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

package io.runon.trading.technical.analysis.subindex.cross;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 *
 * 골든 크로스 데드 크로스 잡기
 * @author macle
 */
public class Cross {
    public enum Type{
        GOLDEN // 골든크로스
        , DEAD // 데드크로스
    }

    /**
     * 크로스 지점 얻기
     * shot 5 이면 long 은 10 15 20 등.
     * 5일 이평선과 20일 이평선의 경우를 생각하면 됨
     * 두 배열의 크기는 반드시 일치 할것
     * @param shotArray 짧은 배열
     * @param longArray 긴배열
     * @param rate 돌파를 확인하는 비율 (겹친 정도로는 돌파로 보기 어려움) 백분율 percent
     * @return 크로스 발생 유형과 위치
     */
    public static CrossIndex getIndex(BigDecimal[] shotArray, BigDecimal [] longArray, BigDecimal rate ){


        int lastIndex = longArray.length -1;


        if( shotArray[lastIndex].compareTo( longArray[lastIndex]) > 0){
            //골득 크로스 검색
            for (int i = lastIndex ; i > 4 ; i--) {
                if(shotArray[i].compareTo(longArray[i]) <= 0 ){
                    return null;
                }
                int gap = gap(longArray, shotArray, i);

                if(gap == -1){
                    continue;
                }

                if(!isRate(longArray, shotArray, i, rate)){
                    return null;
                }

                CrossIndex crossIndex = new CrossIndex();
                crossIndex.type = Type.GOLDEN;
                crossIndex.index = i;
                crossIndex.gap = gap;
                return crossIndex;

            }

        }else if(shotArray[lastIndex].compareTo( longArray[lastIndex]) < 0){
            //데드 크로스 검색
            for (int i = lastIndex ; i > 4 ; i--) {
                if(shotArray[i].compareTo(longArray[i]) >= 0 ){
                    return null;
                }

                int gap = gap(shotArray, longArray, i);

                if(!isRate(shotArray, longArray, i, rate)){
                    return null;
                }
                CrossIndex crossIndex = new CrossIndex();
                crossIndex.type = Type.DEAD;
                crossIndex.index = i;
                crossIndex.gap = gap;
                return crossIndex;

            }
        }

        return null;
    }
    
    private static boolean isRate(BigDecimal [] small, BigDecimal [] large, int index, BigDecimal rate){
        for (int i = index; i <small.length ; i++) {
            BigDecimal length = large[i].subtract(small[i]);
            if(length.divide(small[i], MathContext.DECIMAL128).compareTo(rate) >= 0){
                return true;
            }
        }

        return false;
    }
    
    /**
     *
     * @param small 작은값
     * @param large 큰값
     * @param index 기준위치
     * @return 이전에 발생한 지점부터의 gap, 유효하지 않을경우 -1
     */
    private static int gap(BigDecimal [] small, BigDecimal [] large, int index){

        int count = 0;

        int last = index;
        for (int i = index-1; i > -1 ; i--) {
            //큰값이 작아야함
            if(large[i].compareTo(small[i]) > 0){
                break;
            }

            if(large[i].compareTo(small[i]) < 0){
                count++;
            }
            last = i;
        }


        //값이 교차된 전 개수가 최소 5개는 되어야 함
        if(count < 5){
            return -1;
        }

        return index - last;
    }

}
