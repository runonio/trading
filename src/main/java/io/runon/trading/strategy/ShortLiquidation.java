package io.runon.trading.strategy;
/**
 * 숏청산
 * @author macle
 */
public interface ShortLiquidation<E> {
    boolean isShortLiquidation(E data);
}
