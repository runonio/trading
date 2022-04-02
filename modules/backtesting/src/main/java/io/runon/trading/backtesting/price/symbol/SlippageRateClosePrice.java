package io.runon.trading.backtesting.price.symbol;

import io.runon.trading.Candle;

import java.math.BigDecimal;

/**
 * 슬리피지를 특정 비율로만 계산할때
 * 종가 활용
 * 현재 캔들이 끝나는 시점인 종가에 들어간다고 가정한 방식
 * @author ccsweets
 */
public class SlippageRateClosePrice extends CandleSymbolMapPrice {

    //기본값 0.25%
    private BigDecimal rate = new BigDecimal("0.0025");

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal getBuyPrice(String symbol) {
        Candle candle = candleMap.get(symbol);
        BigDecimal price = candle.getClose();

        if(rate.compareTo(BigDecimal.ZERO) == 0){
            return price;
        }
        return price.add(price.multiply(rate));
    }

    @Override
    public BigDecimal getSellPrice(String symbol) {

        Candle candle = candleMap.get(symbol);
        BigDecimal price = candle.getClose();
        if(rate.compareTo(BigDecimal.ZERO) == 0){
            return price;
        }

        return price.subtract(price.multiply(rate));
    }
}
