package io.runon.trading.strategy;

/**
 * 전략 
 * 거래대금 정보를 포함한 전략
 * @author macle
 */
public interface StrategyOrder<E> {

    Order getPosition(E data);
}
