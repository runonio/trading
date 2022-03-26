package io.runon.trading.backtesting.price.symbol;

import io.runon.trading.Price;

import java.math.BigDecimal;

/**
 * 슬리피지를 특정 비율로만 계산할때
 * 종가 활용
 * @author macle
 */
public class SlippageRatePrice extends MapSymbolPrice<Price> {

    //기본값 0.25%
    private BigDecimal rate = new BigDecimal("0.0025");

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal getBuyPrice(String symbol) {
        Price price = priceMap.get(symbol);
        BigDecimal close = price.getClose();

        if(rate.compareTo(BigDecimal.ZERO) == 0){
            return close;
        }
        return close.add(close.multiply(rate));
    }

    @Override
    public BigDecimal getSellPrice(String symbol) {

        Price price = priceMap.get(symbol);
        BigDecimal close = price.getClose();
        if(rate.compareTo(BigDecimal.ZERO) == 0){
            return close;
        }

        return close.subtract(close.multiply(rate));
    }
}
