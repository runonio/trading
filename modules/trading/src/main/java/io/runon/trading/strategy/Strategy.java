package io.runon.trading.strategy;
/**
 * 전략
 * @author macle
 */
public interface Strategy<E> {

    Position getPosition(E data);
}
