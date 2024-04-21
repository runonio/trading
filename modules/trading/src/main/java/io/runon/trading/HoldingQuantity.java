package io.runon.trading;

import java.math.BigDecimal;
/**
 * @author macle
 */
public interface HoldingQuantity {

    String getId();
    BigDecimal getQuantity();

    void setQuantity(BigDecimal quantity);


}
