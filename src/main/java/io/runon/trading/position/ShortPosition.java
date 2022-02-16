package io.runon.trading.position;
/**
 * @author macle
 */
public interface ShortPosition<T> {
    boolean isShort(T data);
}
