package io.runon.trading.technical.analysis.indicators;

import io.runon.trading.BigDecimals;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.candle.Candles;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 윌리엄스 %R
 * Williams Percent Range
 *
 * R% = - ((H - C)/(H – L)) x 100;
 *
 * 여기서
 * C – 최신 종가
 * L – 주어진 기간동안 최저가
 * H – 주어진 기간동안 최고가
 *
 * @author macle
 */
public class Wpr extends NTimeNumberIndicators<CandleStick> {

    public Wpr(){
        defaultN = 14;
    }
    @Override
    public BigDecimal get(CandleStick[] array, int n, int index) {

        BigDecimal high = Candles.high(array, n, index);

        //고가 - 종가
        BigDecimal up =  high.subtract(array[index].getClose());
        //고가 - 저가
        BigDecimal down = high.subtract(Candles.low(array,n, index));

        return up.multiply(BigDecimals.DECIMAL_100).divide(down,scale, RoundingMode.HALF_UP).multiply(BigDecimals.DECIMAL_M_1);
    }
}