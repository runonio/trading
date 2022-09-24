package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.trading.TimeNumber;
import io.runon.trading.technical.analysis.indicators.EmaOscillator;

/**
 *
 * 가격이 상승하기 전에 거래량이 먼저 상승하는 경우를 우리는 가격 차트상에서 쉽게 본다.
 * 또한 거래량의 상승 없이 전개되는 가격의 상승은 오래 이어지지 못하는 경우를 쉽게 볼 수 있다.
 * 거래량의 움직임은 주가의 움직임에 선행하는 경우가 있다. 그러면 거래량이 어느 정도 상승하는 경우에 매입을 하여야 하며, 어느 정도 하락경우에 매도해야 하는 것인가.
 * 이에 대한 해답을 얻고자 시도한 OBV를 개선한 방법 중의 하나가 바로 이 지표이다.
 *
 *  Chaikin Oscillator는 막크 차이킨(Marc Chaikin)이 새로운 거래량 지표로 개발한 것으로 단기 ADI 이동평균에 장기 ADI 이동평균을 빼서 오실레이터화 한 지표이다.
 *  이 지표는 가격이 상승하기 전에 거래량이 먼저 상승한다는 것을 전제로 만들어졌으며 오실레이터화 해서 보다 빠르게 신호가 나타난다
 *
 *  www.hi-ib.com/systemtrade/st020908view01.jsp
 * @author macle
 */
public class ChaikinOscillator {

    public static int DEFAULT_SHORT_N = 3;
    public static int DEFAULT_LONG_N = 10;

    /**
     * 반드시 ADI를 구하여 사용할것
     */
    public static TimeNumber[] get(TimeNumber[] adiArray){
        return EmaOscillator.get(adiArray, DEFAULT_SHORT_N, DEFAULT_LONG_N);
    }


}
