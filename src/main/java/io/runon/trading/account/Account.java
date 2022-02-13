package io.runon.trading.account;

import java.math.BigDecimal;

/**
 * 게좌
 * @author macle
 */
public interface Account {

    String getId();
    BigDecimal getAssets();
}
