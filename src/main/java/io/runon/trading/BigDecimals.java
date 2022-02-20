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

package io.runon.trading;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 가격변화유형
 * @author macle
 */
public class BigDecimals {

    // -1
    public final static BigDecimal DECIMAL_M_1 = new BigDecimal(-1);

    public final static BigDecimal DECIMAL_2 = new BigDecimal(2);
    public final static BigDecimal DECIMAL_3 = new BigDecimal(3);
    public final static BigDecimal DECIMAL_4 = new BigDecimal(4);
    public final static BigDecimal DECIMAL_100 = new BigDecimal(100);


    //0.5
    public final static BigDecimal DECIMAL_0_5 = new BigDecimal("0.5");

    //1.5
    public final static BigDecimal DECIMAL_1_5 = new BigDecimal("1.5");

    //2.5
    public final static BigDecimal DECIMAL_2_5 = new BigDecimal("2.5");


    public static BigDecimal getMin(BigDecimal num1, BigDecimal num2){
        if(num1 == null){
            return num2;
        }

        if(num2 == null){
            return num1;
        }

        if(num1.compareTo(num2) > 0){
            return num2;
        }

        return num1;
    }

    public static BigDecimal getMax(BigDecimal num1, BigDecimal num2){
        if(num1 == null){
            return num2;
        }

        if(num2 == null){
            return num1;
        }

        if(num1.compareTo(num2) < 0){
            return num2;
        }

        return num1;
    }

    /**
     * BigDecimal 을 활용한 text 얻기
     * @param num BigDecimal
     * @param scale 소수점
     * @return 문자열
     */
    public static String getText(BigDecimal num, int scale){
        if( num == null){
            return "";
        }
        return num.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    public static BigDecimal add(BigDecimal [] array){
        BigDecimal sum = BigDecimal.ZERO;
        for(BigDecimal decimal : array){
            sum = sum.add(decimal);
        }
        return sum;
    }

    public static BigDecimal getChangePercent(BigDecimal previous , BigDecimal close){
        int compare = previous.compareTo(close);

        if(compare == 0){
            return BigDecimal.ZERO;
        }
        BigDecimal change = close.subtract(previous);
        return change.divide(previous,4, RoundingMode.HALF_UP).multiply(DECIMAL_100).stripTrailingZeros();
    }

}
