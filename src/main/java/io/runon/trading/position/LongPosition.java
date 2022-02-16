package io.runon.trading.position;
/**
 * @author macle
 */
public interface LongPosition<T> {
    boolean isLong(T data);
}
