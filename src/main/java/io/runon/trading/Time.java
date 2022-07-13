package io.runon.trading;

import java.util.Comparator;

/**
 * 시간정보
 * @author macle
 */
public interface Time {
    Comparator<Time> SORT_ASC = Comparator.comparing(Time::getTime);
    Comparator<Time> SORT_DESC = (t1, t2) -> Long.compare(t2.getTime(), t1.getTime());
    long getTime();
}
