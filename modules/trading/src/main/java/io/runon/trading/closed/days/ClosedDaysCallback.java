package io.runon.trading.closed.days;

import com.seomse.commons.callback.StrCallback;

/**
 * @author macle
 */
public interface ClosedDaysCallback {
    void callbackClosedDays(String beginYmd, String endYmd, StrCallback strCallback);
}
