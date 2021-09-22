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
 * 볼륨 정보
 * @author ccsweets
 */
public class VolumeData {
    long time;
    double volume;
    String color;

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

    /**
     * 거래량을 얻는다.
     * @return 거래량
     */
    public double getVolume() {
        return volume;
    }

    /**
     * 거래량을 설정한다.
     * @param volume 거래량
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * 색깔을 얻는다.
     * @return 색깔
     */
    public String getColor() {
        return color;
    }

    /**
     * 색깔을 설정한다.
     * @param color 색깔
     */
    public void setColor(String color) {
        this.color = color;
    }
}
