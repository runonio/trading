package io.runon.trading.data.management;

import io.runon.trading.data.Futures;
import io.runon.trading.data.file.PathTimeLine;

/**
 * @author macle
 */
public interface FuturesDailyOutParam {
    String [] getLines(Futures futures, String beginYmd, String endYmd );

    PathTimeLine getPathTimeLine();
    FuturesPathLastTime getFuturesPathLastTime();

    /**
     * 증권사별로 상장폐지 된 종목은 사용할 수 없는 경우가 있다
     * 지금 상장중인 종목만 사용할지에 대한 여부
     * @return 현제 상장된 종목만 활용할지
     */
    boolean isListed();

    int getNextDay();

    void sleep();

    /**
     * 거래소 얻기
     * 선물은 거래소를 여러개를 사용하는 경우가 없을것으로 예상한다.
     * 하나의 구현체는 하나의 거래소만 처리하는 경우가 대부분으로 보인다.
     * @return 거래소
     */
    String getExchange();
}
