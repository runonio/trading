package io.runon.trading.technical.analysis.indicators.market;

import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.indicators.EmaOscillator;

/**
 * Advancing Decline Issue
 *
 * 상승 종목수 - 하락종목수 이평선을 활용한 오실레이터
 *
 * McClellan Oscillator를 계산하기 위해서는 먼저 매일의 상장종목수와 하락종목수의 차이를 계산하여야 한다. 이것의 19일 지수이동평균에서 39일 지수이동평균을 차감한 것이 McClellan Oscillator이다.
 *
 * @author macle
 */
public class McClellanOscillator {



    public static int DEFAULT_SHORT_N = 19;
    public static int DEFAULT_LONG_N = 39;

    /**
     * 반드시 ADI를 구하여 사용할것
     */
    public static TimeNumber[] get(TimeNumber[] adIssueArray) {
        return EmaOscillator.get(adIssueArray, DEFAULT_SHORT_N, DEFAULT_LONG_N);
    }
}
