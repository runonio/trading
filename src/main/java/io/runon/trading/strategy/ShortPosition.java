package io.runon.trading.strategy;
/**
 * @author macle
 */
public interface ShortPosition<E> {
    boolean isShort(E data);
}
