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
package io.runon.trading.view;
/**
 * 라인 정보
 * @author ccsweets
 */
public class LineData {
    double price;
    long time;

    /**
     * 가격을 얻는다.
     * @return 가격
     */
    public double getPrice() {
        return price;
    }

    /**
     * 가격을 설정한다.
     * @param price 가격
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * 시간을 얻는다.
     * @return 시간
     */
    public long getTime() {
        return time;
    }

    /**
     * 시간을 설정한다.
     * @param time 시간
     */
    public void setTime(long time) {
        this.time = time;
    }
}
