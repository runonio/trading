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
 * 마커 정보
 * @author ccsweets
 */
@SuppressWarnings("ClassCanBeRecord")
public class MarkerData {
    /**
     * 마커 유형
     * lightweight-charts js 사용하는 변수그대로 이므로 소문자 사용
     */
    public enum MarkerType{belowBar,aboveBar,inBar}

    /**
     * 마커 모양
     * lightweight-charts js 사용하는 변수그대로 이므로 소문자 사용
     */
    public enum MarkerShape{circle ,square , arrowUp , arrowDown }
    private final long time;
    private final String color;
    private final String text;
    private final String id;
    private final MarkerType markerType;
    private final MarkerShape markerShape;

    /**
     * 시간을 설정한다.
     * @return 시간
     */
    public long getTime() {
        return time;
    }

    /**
     * 색깔을 설정한다.
     * @return 색깔
     */
    public String getColor() {
        return color;
    }

    /**
     * 문자를 설정한다.
     * @return 문자
     */
    public String getText() {
        return text;
    }

    /**
     * ID를 설정한다.
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * 마커 유형을 얻는다.
     * lightweight-charts js 사용하는 변수그대로 이므로 소문자 사용
     * @return 마커 유형
     */
    public MarkerType getMarkerType() {
        return markerType;
    }

    /**
     * 마커 모양을 얻는다.
     * lightweight-charts js 사용하는 변수그대로 이므로 소문자 사용
     * @return 마커모양
     */
    public MarkerShape getMarkerShape() {
        return markerShape;
    }

    /**
     * Constructor
     * @param time 시간
     * @param color 색깔
     * @param text 문자
     * @param id ID
     * @param markerType 마커유형
     * @param markerShape 마커모양
     */
    public MarkerData(long time, String color, String text, String id, MarkerType markerType, MarkerShape markerShape) {
        this.time = time;
        this.color = color;
        this.text = text;
        this.id = id;
        this.markerType = markerType;
        this.markerShape = markerShape;
    }
}
