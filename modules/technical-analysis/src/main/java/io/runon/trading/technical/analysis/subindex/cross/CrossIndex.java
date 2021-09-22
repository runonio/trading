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

/**
 * 골든크로스 혹은 데드크로스 발생 유형과 위치
 * @author macle
 */
public class CrossIndex {

    Cross.Type type;
    int index;

    int gap;
    
    /**
     * 크로스 유형얻기
     * @return 골드크로스 or 데드 크로스
     */
    public Cross.Type type() {
        return type;
    }

    /**
     * 최근 크로스 발생지점 얻기
     * @return 최근 크로스 발생지점
     */
    public int index() {
        return index;
    }

    /**
     * 마지막 발생시점으로 부터의 거리 얻기
     * @return 마지막 발생시점으로 부터의 거리
     */
    public int gap() {
        return gap;
    }
}
