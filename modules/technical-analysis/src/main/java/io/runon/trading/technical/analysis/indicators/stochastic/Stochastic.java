package io.runon.trading.technical.analysis.indicators.stochastic;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.indicators.ma.Ema;
import io.runon.trading.technical.analysis.indicators.ma.MovingAverage;
import io.runon.trading.technical.analysis.indicators.ma.Sma;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 스토캐스틱
 * wiki.hash.kr/index.php/%EC%8A%A4%ED%86%A0%EC%BA%90%EC%8A%A4%ED%8B%B1
 *
 * 스토캐스틱(Stochastic)은 주식투자에서 기술적 분석에 사용되는 보조지표이다. 당일의 종가 위치를 백분율로 표시하여 알려주며,
 * 시장이 상승추세의 막바지에 다다를수록 종가가 당일 고가에 근접하고, 하락추세의 막바지에 다다를수록 종가가 당일 저가에 근접하는 속성을 이용한다. 해당 지표는 기술 분석가인 조지 레인(George Lane)이 개발했다.
 *
 * 스토캐스틱의 정식 명칭은 스토캐스틱 오실레이터(Stochastics Oscillator)이다.
 * 1950년대에 주식 중개 일을 했던 조지 레인에 의해 널리 보급된 주식 분석 보조지표이다
 * . 그리고 국내에서는 고승덕 변호사에 의해 유명해졌다.
 * 스토캐스틱은 주가를 % 값으로 바꾸어 주가의 변화 추세를 나타내는 지표이다.
 * 스토캐스틱은 최저가와 최고가의 범위가 있기 때문에, 그 값은 무조건 0~100% 사이의 값을 가지게 된다.
 * 만약 100%라면 설정한 일수 내에서 매수세가 가장 강한 경우며, 반대로 0%면 매도세가 가장 강한 경우이다.
 * 또한 스토캐스틱은 패스트(fast)와 슬로우(slow)로 구분된다. 둘 다 모두 %K값과 %D값을 통해 매수와 매도 타이밍을 잡게 된다.
 * 하지만 스토캐스틱 패스트는 단기매매에 유용하지만, 주가에 민감하게 반응을 하기에 시그널이 너무 자주 발생하며, 거짓 신호가 자주 발생하는 단점이 있다.
 * 그렇기에 변화가 많고 거짓 신호가 많은 스토캐스틱 패스트의 단점을 보완하기 위해 만들어진 것이 스토캐스틱 슬로우이다.
 * 단순 가격으로 만들어진 패스트와 달리 이동평균을 구한 슬로우는 보다 완만한 그래프를 만들어 낼 수 있다.
 *
 * 스토캐스틱 %K = (현재가격 - N일중 최저가)/(N일중 최고가 - N일중 최저가) * 100
 *
 * 스토캐스틱 %D = m일 동안 %K 평균 = Slow %K
 *
 * Slow %D = %D 의 이동평균
 * 이동평균은 기본으로 SMA를 사용하지만 EMA를 사용해도 괜찮음
 *
 *
 * @author macle
 */
@Setter
public class Stochastic {

    public static final int DEFAULT_K = 5;

    public static final int DEFAULT_D = 3;

    public static final int DEFAULT_SLOW_D = 3;

    private int k = DEFAULT_K;
    private int d = DEFAULT_D;
    private int slowD = DEFAULT_SLOW_D;
    private MovingAverage.Type type = MovingAverage.Type.EMA;
    public Stochastic(){
    }

    public StochasticData [] get(CandleStick [] array, int resultLength){
        return get(array, k, d, slowD, array.length - resultLength, array.length, type);
    }
    public StochasticData [] get(CandleStick [] array, int startIndex, int end){
        return get(array, k, d, slowD, startIndex, end, type);
    }


    public StochasticData [] getEma(CandleStick [] array){
        return get(array, DEFAULT_K, DEFAULT_D, DEFAULT_SLOW_D, 0, array.length, MovingAverage.Type.EMA);
    }
    public StochasticData [] getEma(CandleStick [] array, int resultLength){
        return get(array, DEFAULT_K, DEFAULT_D, DEFAULT_SLOW_D, array.length - resultLength, array.length, MovingAverage.Type.EMA);
    }

    public StochasticData [] getEma(CandleStick [] array, int k, int d, int sd, int resultLength){
        return get(array, k, d, sd, array.length - resultLength, array.length, MovingAverage.Type.EMA);
    }

    public StochasticData [] getEma(CandleStick [] array, int k, int d, int sd, int startIndex, int end){
        return get(array, k, d, sd, startIndex, end, MovingAverage.Type.EMA);
    }

    public StochasticData [] getSma(CandleStick [] array){
        return get(array, DEFAULT_K, DEFAULT_D, DEFAULT_SLOW_D, 0, array.length, MovingAverage.Type.SMA);
    }

    public StochasticData [] getSma(CandleStick [] array, int resultLength){
        return get(array, DEFAULT_K, DEFAULT_D, DEFAULT_SLOW_D, array.length - resultLength, array.length, MovingAverage.Type.SMA);
    }

    public StochasticData [] getSma(CandleStick [] array, int k, int d, int sd, int resultLength){
        return get(array, k, d, sd, array.length - resultLength, array.length, MovingAverage.Type.SMA);
    }

    public StochasticData [] getSma(CandleStick [] array, int k, int d, int sd, int startIndex, int end){
        return get(array, k, d, sd, startIndex, end, MovingAverage.Type.SMA);
    }

    public StochasticData [] get(CandleStick [] array, int k, int d, int sd, int startIndex, int end, MovingAverage.Type type){
        if(startIndex < 0){
            startIndex = 0;
        }

        if(end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        BigDecimal [] kArray = new BigDecimal[resultLength];

        for (int i = 0; i < resultLength; i++) {
            int last = i+startIndex;

            int endJ = last + 1;
            int startJ = endJ - k;

            if(startJ < 0){
                startJ = 0;
            }

            BigDecimal close = array[last].getClose();
            BigDecimal low = array[startJ].getLow();
            BigDecimal high = array[startJ].getHigh();

            for (int j = startJ+1; j < endJ ; j++) {
                CandleStick candleStick =array[j];
                if(high.compareTo(candleStick.getHigh()) > 0){
                    high = candleStick.getHigh();
                }

                if(low.compareTo(candleStick.getLow()) < 0){
                    low = candleStick.getLow();
                }
            }
            //분모
            BigDecimal denominator = high.subtract(low);
            if(denominator.compareTo(BigDecimal.ZERO) == 0){
                kArray[i] = BigDecimals.DECIMAL_100;
                continue;
            }

            //분자
            BigDecimal numerator = close.subtract(low);
            kArray[i] = BigDecimals.DECIMAL_100.multiply(numerator).divide(denominator, MathContext.DECIMAL128);
        }

        BigDecimal [] dArray;
        BigDecimal [] sdArray;

        if(type == MovingAverage.Type.SMA){
            dArray = Sma.getArray(kArray, d, resultLength);
            sdArray = Sma.getArray(dArray, sd, resultLength);
        }else if(type == MovingAverage.Type.EMA){
            dArray = Ema.getArray(kArray, d, resultLength);
            sdArray = Ema.getArray(dArray, sd, resultLength);
        }else{
            throw new IllegalArgumentException("type(SMA, EMA) unsupported: " + type.toString());
        }

        StochasticData [] daraArray = new StochasticData[resultLength];
        for (int i = 0; i < resultLength; i++) {
            StochasticData data = new StochasticData();
            data.time =  array[i+startIndex].getTime();
            data.k = kArray[i];
            data.d = dArray[i];
            data.slowD = sdArray[i];
            daraArray[i] = data;
        }

        return daraArray;
    }

}
