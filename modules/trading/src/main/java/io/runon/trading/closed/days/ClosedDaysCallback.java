package io.runon.trading.closed.days;

import com.seomse.commons.callback.StrCallback;

/**
 * @author macle
 */
public interface ClosedDaysCallback {
    String [] callbackClosedDays(String beginYmd, String endYmd, StrCallback strCallback);
}
