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
}
