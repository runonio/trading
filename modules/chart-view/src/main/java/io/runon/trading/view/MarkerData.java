
package io.runon.trading.view;

import java.util.Comparator;

/**
 * 마커 정보
 * @author ccsweets
 */
@SuppressWarnings("ClassCanBeRecord")
public class MarkerData {

    public final static Comparator<MarkerData> SORT_TIME = Comparator.comparingLong(o -> o.time);

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
