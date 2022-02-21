package io.runon.trading.strategy;
/**
 * 숏 포지션 종료
 * @author macle
 */
public interface ShortClose<E> {
    boolean isShortClose(E data);
}
