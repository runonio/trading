package io.runon.trading.strategy;
/**
 * 행동 유령 분류
 * @author macle
 */
public enum Position {
    NONE, // 아무런 행동하지않음
    LONG, //롱포지션 -> 숏포지션 매도, 롱 포지션 매수
    LONG_LIQUIDATION, // 롱 포지션 청산
    SHORT, //숏포지션 -> 롱포지션 매도, 숏포지선 매수
    SHORT_LIQUIDATION, // 숏 포지션 청산
    LIQUIDATION // 전부 청산
}