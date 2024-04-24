package io.runon.trading.backtesting.price;

import java.math.BigDecimal;

/**
 * 종목별 가격 얻기
 * @author macle
 */
public interface IdPrice {

    BigDecimal getPrice(String id);
    BigDecimal getBuyPrice(String id);
    BigDecimal getSellPrice(String id);
}
