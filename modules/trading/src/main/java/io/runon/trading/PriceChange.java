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
 * 가격과 가격 변화
 * @author macle
 */
public interface PriceChange extends ChangeRate, Price{

    /**
     * previous 기준
     * (일별이면 전 거래일, 분봉이면 전봉))
     * @return 변동가격
     */
    BigDecimal getChange();

    /**
     * (일별이면 전 거래일, 분봉이면 전봉)
     * @return 전 거래 가격
     */
    BigDecimal getPrevious();
}
