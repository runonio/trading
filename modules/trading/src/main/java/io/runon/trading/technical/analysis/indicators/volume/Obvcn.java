package io.runon.trading.technical.analysis.indicators.volume;

import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.indicators.NTimeNumberIndicators;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * obv에 변화량 가중치를 준다
 * n의 수만큼 평준화 한다
 * @author macle
 */
public class Obvcn extends NTimeNumberIndicators<TradeCandle> {

    public Obvcn(){
        defaultN = 50;
        scale = 4;
    }


    @Override
    public BigDecimal get(TradeCandle[] array, int n, int index) {


        int end = index+1;
        int startIndex = end -n;
        if(end > array.length){
            end = array.length;
        }

        if(startIndex < 0){
            startIndex = 0;
        }

        int length = end - startIndex;
        if(length == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;

        for (int i = startIndex; i < end; i++) {
            TradeCandle tradeCandle = array[i];

            BigDecimal cr = Obvc.get(tradeCandle);
            sum = sum.add(cr);
        }

        return sum.divide(new BigDecimal(length), MathContext.DECIMAL128);

    }
}