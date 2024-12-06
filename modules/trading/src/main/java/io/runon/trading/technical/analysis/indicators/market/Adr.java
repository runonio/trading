package io.runon.trading.technical.analysis.indicators.market;

import io.runon.commons.config.Config;
import io.runon.trading.BigDecimals;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.IdCandles;
import io.runon.trading.technical.analysis.candle.IdCandleTimes;
import io.runon.trading.technical.analysis.candle.TradeCandle;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 상승 하락 종목의 비율
 * Advancing Decline ratio
 *
 * A/D ratio는 상승한 종목과 하락한 종목의 비율을 나타낸다. 등락종목수의 누적 차인 ADL과 달리 등락비율을 말한다. 일반적으로 ADR는 매일매일의 ADR를 이동평균하여 이용한다. 계산은 상승종목수를 하락종목수로 나눠 백분율을 한다. 대개 20일 이동평균을 사용한다. 천정권보다는 바닥권에서 위력을 발휘한다. 20일 동안의 상승 하락 종목수를 이용할 수도 있다. 이 비율이 120%를 웃돌면 경제상황으로 약세장을 예상해볼 수 있다. 70% 이하면 바닥권으로 상승장세로 전환되는 경우가 많다.
 *
 * A/D ratio는 A/D issues와 비교할 때 시장폭을 보여준다는 점에서 유사하다. A/D issues 는 상승치와 하락치를 차감하지만 A/D ratio는 나눈 값이다. 비율 (나눈값)의 이점은 거래소에서 거래된 주식의 수에 상관없이 수치가 일정하다는 것이다. (거래소에서 거래된 주식수는 일정하게 증가하게 마련이다.)
 * A/D ratio 의 이동평균은 종종 시장의 과매수/과매도 지표로 활용된다. 수치가 높을수록 흐름이 지나치며(Excessive rally) 조정을 보일 가능성이 커진다. 이와 유사하게 수치가 낮은 경우는 과매도 된 상태를 나타내며 기술적인 매수 (technical rally)를 권한다. 그렇지만 명심해둘 것은 시장이 극도로 과매수/과매도 된 상태처럼 보일지라도 그런식으로 상당기간 유지될 수 있다는 것이다. 현명한 투자가라면 거래포지션을 정하기 전에 당신의 신념을 확신시켜 줄 가격이 올 때까지 기다리는 태도가 필요하다. 매일매일 A/D ratio 의 변동폭은 이동평균치로 균질화함으로서 제거된다.
 *
 * 참조자료
 * www.hi-ib.com/systemtrade/st02090801view02.jsp
 *
 * 대게 20일 이동평균을 활용한다.
 * Ema 와 Sma 중 맞는걸 골라서 사용
 *
 * 0 ~ 500
 * @author macle
 */
public class Adr extends MarketIndicators<TimeNumber> {


    public static final BigDecimal MAX = new BigDecimal(Config.getConfig("adr.max", "200"));

    public Adr(IdCandles[] idCandles) {
        super(idCandles);

    }
    public Adr(IdCandleTimes idCandleTimes) {
        super(idCandleTimes);

    }
    @Override
    public TimeNumber getData(int index) {
        TimeNumberData data = new TimeNumberData();
        long time = times[index];

        data.setTime(time);
        int searchLength = searchIndex(index);

        int advancing = 0;
        int decline = 0;

        for(IdCandles symbolCandle : idCandles){
            TradeCandle[] candles = symbolCandle.getCandles();
            int openTimeIndex = Candles.getOpenTimeIndex(candles, time, searchLength);
            if(openTimeIndex == -1){
                continue;
            }

            TradeCandle candle = candles[openTimeIndex];
            if(minAmount != null &&  candle.getAmount().compareTo(minAmount) < 0) {
                continue;
            }

            BigDecimal change = candle.getChange();
            if(change == null){
                change =candle.getChangeRate();
            }

            if(change == null){
                continue;
            }

            int compare = change.compareTo(BigDecimal.ZERO);

            if(compare == 0){
                continue;
            }

            if(compare > 0){
                advancing++;
            }else{
                decline++;
            }

        }
        if(advancing == 0){
            data.setNumber(BigDecimal.ZERO);
            return data;
        }
        if(decline == 0){
            data.setNumber(MAX);
            return data;
        }

        data.setNumber(getAdr(advancing, decline).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros());
        return data;
    }

    public static BigDecimal getAdr(int advancing, int decline){
        if(advancing == 0){
            return BigDecimal.ZERO;
        }
        if(decline == 0){
            return MAX;
        }

        BigDecimal adr = new BigDecimal(advancing).divide(new BigDecimal(decline), MathContext.DECIMAL128).multiply(BigDecimals.DECIMAL_100);

        if(adr.compareTo(MAX) > 0 ){
            return MAX;
        }

        return adr;
    }

    @Override
    public TimeNumber[] newArray(int startIndex, int end) {
        TimeNumber[] array = new TimeNumber[end - startIndex];

        for (int i = 0; i < array.length ; i++) {
            array[i] = getData(i + startIndex);
        }
        return array;
    }
}
