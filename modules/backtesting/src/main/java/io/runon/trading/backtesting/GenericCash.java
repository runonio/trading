
package io.runon.trading.backtesting;

import java.math.BigDecimal;

/**
 * 주문 현금
 * @author macle
 */
public interface GenericCash<E> {
    BigDecimal getCash(E data);
}
