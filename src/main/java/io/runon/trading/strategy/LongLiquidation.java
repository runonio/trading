package io.runon.trading.strategy;
/**
 * 롱청산
 * @author macle
 */
public interface LongLiquidation<E> {
    boolean isLongLiquidation(E data);
}
