package io.runon.trading.strategy;
/**
 * @author macle
 */
public interface LongPosition<E> {
    boolean isLong(E data);
}
