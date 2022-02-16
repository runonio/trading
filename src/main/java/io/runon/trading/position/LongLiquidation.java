package io.runon.trading.position;
/**
 * 롱청산
 * @author macle
 */
public interface LongLiquidation<T> {
    boolean isLongLiquidation(T data);
}
