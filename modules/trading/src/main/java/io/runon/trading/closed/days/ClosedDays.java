package io.runon.trading.closed.days;
/**
 * 휴장일 관련 처리
 * 메모리, DB조회등의 다양한 방법이 있을 수 있음
 * @author macle
 */
public interface ClosedDays {

    /**
     * 휴장일여부 얻기
     * @param ymd yyyyMMdd
     * @return 휴장일 여부
     */
    boolean isClosedDay(String ymd);

}
