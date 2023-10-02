package io.runon.trading.strategy;
/**
 * 롱포지션 종료
 * @author macle
 */
public interface LongClose<E> {
    boolean isLongClose(E data);
}
