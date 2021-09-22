/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.runon.trading.technical.analysis.subindex.ichimoku;

/**
 * 일목균형 데이터
 * 기간의 시간/전환/기준/후행/선행1/선행2 정보를 얻는다
 * -1 일시 없는 데이터
 * @author ccsweets
 */
public class IchimokuData {
    long time;
    private double reverse = -1.0;
    private double standard = -1.0;
    private double lagging = -1.0;
    private double leading1 = -1.0;
    private double leading2 = -1.0;

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
     * 전환선을 얻는다
     * @return 전환선
     */
    public double getReverse() {
        return reverse;
    }

    /**
     * 전환선 설정
     * @param reverse 전환선
     */
    public void setReverse(double reverse) {
        this.reverse = reverse;
    }

    /**
     * 기준선을 얻는다
     * @return 기준선
     */
    public double getStandard() {
        return standard;
    }

    /**
     * 기준선 설정
     * @param standard 기준선
     */
    public void setStandard(double standard) {
        this.standard = standard;
    }

    /**
     * 후행스팬을 얻는다
     * @return 후행스팬
     */
    public double getLagging() {
        return lagging;
    }

    /**
     * 후행스팬을 설정한다
     * @param lagging  후행스팬
     */
    public void setLagging(double lagging) {
        this.lagging = lagging;
    }

    /**
     * 선행스팬1을 얻는다
     * @return 선행선1
     */
    public double getLeading1() {
        return leading1;
    }

    /**
     * 선행스팬1을 설정한다
     * @param leading1  선행스팬1
     */
    public void setLeading1(double leading1) {
        this.leading1 = leading1;
    }

    /**
     * 선행스팬2를 설정한다
     * @return 선행스팬2
     */
    public double getLeading2() {
        return leading2;
    }

    /**
     * 선행스팬2를 얻는다
     * @param leading2 선행스팬2
     */
    public void setLeading2(double leading2) {
        this.leading2 = leading2;
    }
}
