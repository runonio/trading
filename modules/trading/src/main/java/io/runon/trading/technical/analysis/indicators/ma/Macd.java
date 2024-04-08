package io.runon.trading.technical.analysis.indicators.ma;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;

import java.math.BigDecimal;

/**
 *
 * MACD
 * Moving Average Convergence Divergence
 * namu.wiki/w/MACD
 *
 * 장단기 이동평균선간의 차이를 이용하여 매매신호를 포착하려는 기법으로 제럴드 아펠(Gerald Appel)에 의해 개발되었다. 오실레이터는 토마스 아스프레이에 의해 추가되었다.
 * 흔히 주가추세의 힘, 방향성, 시간을 측정하기 위해 사용된다.
 *
 * MACD의 원리는 장기 이동평균선과 단기이동평균선이 서로 멀어지게 되면(divergence) 언젠가는 다시 가까워져(convergence) 어느 시점에서 서로 교차하게 된다는 성질을 이용하여 두 개의 이동평균선이 멀어지게 되는 가장 큰 시점을 찾고자 하는 것이다.
 *
 * 이 방법은 장단기 이동평균선의 교차점을 매매신호로 보는 이동평균 기법의 단점인 시차(time lag) 문제를 극복할 수 있는 장점을 지닌다. EMA 롱 숏의 매매신호 전에, MACD 시그널 매매신호가 먼저 나오기 때문이다. 하지만 이와같은 보완에도 불구하고 현실적으로 시차문제를 완전히 극복하는 것은 힘들다. 이동평균선의 개념자체가 가격 데이터를 요약하여 지난 추세를 보고자 하는 것이라 MACD 시그널 역시 실시간 가격이 떨어지면서 나타나기 때문이다. 더하여 MACD 시그널 매매신호는 시차 문제를 줄였다는 장점이 있지만, 그만큼 속임신호 량이 늘어난다는 단점을 안게 된다.
 *
 * 장기 지수이동평균선과 단기 지수이동평균선의 벌어진 차이를 산출하여 작성된 MACD곡선과 이 MACD 곡선을 다시 지수이동평균으로 산출하여 작성한 시그널(signal) 곡선이 교차함으로써 발생되는 신호를 매매신호로 본다.이동평균의 차이를 다시 이동평균으로 산출할 경우 시그널 곡선은 어느 시점에서 두 이동평균의 차이가 가장 최대가 되는지를 쉽게 판단할 수 있게 한다.그러므로 MACD 곡선과 시그널 곡선이 교차하는 시점이 장기 지수이동평균과 단기 지수이동평균의 차이가 가장 큰 시점이 된다.
 *
 * 참고로 MACD선은 기본 매개변수의 경우 5일 이동평균선과 비슷하게 움직이며, 시그널선은 10일 이동평균선과 비슷하게 움직인다. 지표값의 양음을 결정하는 0선은 60일 이동평균선과 비슷한 움직임을 보인다.
 *
 * 1.1. 기본 공식[편집]
 * MACD : 12일 지수이동평균 - 26일 지수이동평균
 * 시그널 : MACD의 9일 지수이동평균
 * 오실레이터 : MACD값 - 시그널값
 *  12, 26, 9 를 많이사용하지만  5, 34, 7 도 믾이 사용된다.
 *
 * 1.2. 추세의 지속과 전환[편집]
 * 일반적으로 주가의 추세와 MACD오실레이터의 크기가 역행하는 경우에 곧 추세의 전환이 일어난다고 알려져 있다. 주가가 상승하는 추세이지만 전고점과 비교해서 지금고점에서의 오실레이터값이 작으면 슬슬 하락세로 돌아설것에 대비하여야 한다. 당연히 주가가 하락추세이어도 전저점의 오실레이터값에 비해서 지금의 저점에서의 오실레이터값이 작으면 곧 상승추세로 돌아설것에 대비하여야 한다.
 *
 * 1.3. 매매신호[편집]
 * MACD가 양으로 증가하면 매수한다.
 * MACD가 시그널을 골드크로스하면 매수한다.
 * MACD가 0선을 상향돌파하면 매수한다.
 *
 * 0선 위에서의 MACD상승은 신뢰성이 높지만 0선 밑에서의 MACD상승은 신뢰성이 낮다. 0선 위에서의 MACD하락은 주가가 오르는 경우가 많다.
 * 일봉MACD에서의 속임수를 피하기 위해 주봉의 MACD를, 주봉MACD에서의 속임수를 피하기 위해 월봉MACD를 참조하면 신뢰성이 높아진다.
 *
 * 초보자는 0선 위에서 MACD가 상승하며 오실레이터가 양수일 때 수익을 내기 쉽다.
 *
 * 저점을 깬 장대 양봉 출현시 골드크로스 된다. 이때 매수하는 것이 아니라 다음날이나 다다음날 한 템포 쉬어가는 작은 캔들이 발생할 때가 있는데 전날 장대양봉의 고점 내지 종가를 찍을 때 매수하자.
 * 이는 저점에서 매집한 세력이 주가를 끌어올리면서 장대 양봉을 만드는 것인데, 장대 양봉을 만든 다음날 주가가 특정 가격 이하로 내려가지 않으면 주가를 그대로 내버려 두기 때문이다. 그러면 다음 날 상승을 기대했던 개미는 거래량도 줄고 호가창에 매수세가 전혀 붙지 않으니 이익 실현을 위해 매도를 한다. 이때 나오는 물량을 세력이 거둬들이면서 주가관리를 한다.
 * 이 물량을 거둬들인 세력은 본격적으로 주가를 재상승시키는데 이때를 노릴 수 있는게 저점에서의 장대 양봉 발생시 나타나는 MACD 교차이다.
 *
 * @author macle
 */
public class Macd {

    public static final int DEFAULT_SHORT_N = 12;
    public static final int DEFAULT_LONG_N = 26;
    public static final int DEFAULT_SIGNAL_N = 9;

    public static MacdData [] get(BigDecimal [] array){
        return get(Ema.getArray(array, DEFAULT_SHORT_N, array.length), Ema.getArray(array, DEFAULT_LONG_N, array.length), DEFAULT_SIGNAL_N);
    }
    public static MacdData [] get(BigDecimal [] shortEmaArray, BigDecimal [] longEmaArray, int signalN){

        int length = shortEmaArray.length;

        BigDecimal [] macdArray = new BigDecimal[length];
        for (int i = 0; i < length ; i++) {
            macdArray[i] = shortEmaArray[i].subtract(longEmaArray[i]);
        }
        BigDecimal [] signalArray = Ema.getArray(macdArray, signalN, length);

        MacdData [] dataArray = new MacdData[length];
        for (int i = 0; i < length ; i++) {
            MacdData macdData = new MacdData();
            macdData.macd = macdArray[i];
            macdData.signal = signalArray[i];
            dataArray[i] = macdData;
        }
        return dataArray;
    }

    public static MacdData [] get(TimeNumber [] array){
        return get(Ema.getTimeNumbers(array, DEFAULT_SHORT_N, array.length), Ema.getTimeNumbers(array, DEFAULT_LONG_N, array.length), DEFAULT_SIGNAL_N);
    }

    public static MacdData [] get(TimeNumber[] shortEmaArray, TimeNumber [] longEmaArray, int signalN){

        int length = shortEmaArray.length;

        BigDecimal [] macdArray = new BigDecimal[length];
        for (int i = 0; i < length ; i++) {
            macdArray[i] = shortEmaArray[i].getNumber().subtract(longEmaArray[i].getNumber());
        }
        BigDecimal [] signalArray = Ema.getArray(macdArray, signalN, length);

        MacdData [] dataArray = new MacdData[length];
        for (int i = 0; i < length ; i++) {
            MacdData macdData = new MacdData();
            macdData.setTime(shortEmaArray[i].getTime());
            macdData.macd = macdArray[i];
            macdData.signal = signalArray[i];
            macdData.oscillator =  macdData.macd.subtract(macdData.signal).stripTrailingZeros();
            dataArray[i] = macdData;
        }
        return dataArray;
    }


    public static TimeNumber [] getOscillators(MacdData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            MacdData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.oscillator);
        }

        return timeNumbers;
    }

    public static TimeNumber [] getSignals(MacdData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            MacdData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.signal);
        }

        return timeNumbers;
    }

}
