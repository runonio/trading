package io.runon.trading.account;

import java.math.BigDecimal;

/**
 * 게좌
 * @author macle
 */
public interface Account {

    /**
     * 계좌 아이디 얻기
     * @return 계좌 아이디
     */
    String getId();

    /**
     * 총 자산얻기
     * @return 총자산
     */
    BigDecimal getAssets();

    /**
     * 현금얻기 
     * @return 현금
     */
    BigDecimal getCash();
}
