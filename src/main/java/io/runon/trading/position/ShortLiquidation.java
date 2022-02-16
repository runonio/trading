package io.runon.trading.position;
/**
 * 숏청산
 * @author macle
 */
public interface ShortLiquidation<T> {
    boolean isShortLiquidation(T data);
}
