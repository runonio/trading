package io.runon.trading;
/**
 * 행동 유령 분류
 * @author macle
 */
public enum Position {
    LONG, //롱 숏포지션 매도, 롱 포지션 매수
    SHORT, //숏 롱포지션 매도, 숏포지선 매수
    HOLDING, // 아무런 행동하지않음
    LONG_SELL, // 롱 포지션 매도
    SHORT_SELL, // 숏 포지션 매도
    SELL //전부매도 롱포지션 숏포지션 둘다 매도
}