package io.runon.trading.closed.days;

import io.runon.trading.data.YmdSet;

/**
 * @author macle
 */
public class ClosedDaysSet extends YmdSet implements ClosedDays{

    @Override
    public boolean isClosedDay(String ymd) {
        return contains(ymd);
    }
}
