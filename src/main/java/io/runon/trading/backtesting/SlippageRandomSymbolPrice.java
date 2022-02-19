package io.runon.trading.backtesting;

import io.runon.trading.Candle;
import io.runon.trading.SymbolPrice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 슬리피지를 고민한 종목별가격
 * 매수할때는 종가와 고가 사이의 가격중 랜덤한 가격을 활용 (고가에 매수할 확률이 높음)
 * 매도할때는 종가와 저가 사이의 가겨중 랜덤한 가격을 활용 (저가에 매도할 확율이 높음)
 * @author macle
 */
public class SlippageRandomSymbolPrice implements SymbolPrice {

    private final Map<String, Candle> candleMap = new HashMap<>();

    public void setCandle(String symbol, Candle candle){
        candleMap.put(symbol, candle);
    }


    private BigDecimal minRate = new BigDecimal("0.2");

    /**
     * 매수 혹은 매도의 최소 비율
     * 0 ~ 1사이로 설정
     * 1이면 고가로 매수, 저가로 매도
     * 0 이면 구매할때 종가와 고가 사이의 랜덤
     * 0.5 이면 종가와 고가 사이의 중간값 부터 고가 사이의 랜덤
     * @param minRate 최소비율
     */
    public void setMinRate(BigDecimal minRate) {
        if(minRate.compareTo(BigDecimal.ONE) > 0){
            throw new IllegalArgumentException("between 0 and 1: " + minRate.toPlainString());
        }
        this.minRate = minRate;
    }

    @Override
    public BigDecimal getPrice(String symbol) {
        return candleMap.get(symbol).getClose();
    }

    @Override
    public BigDecimal getBuyPrice(String symbol) {
        Candle candle = candleMap.get(symbol);
        BigDecimal gap = candle.getHigh().subtract(candle.getClose());
        BigDecimal min = gap.multiply(minRate);
        BigDecimal random = BigDecimal.valueOf(new Random().nextDouble(gap.subtract(min).doubleValue()));
        return candle.getClose().add(min).add(random);
    }

    @Override
    public BigDecimal getSellPrice(String symbol) {
        Candle candle = candleMap.get(symbol);
        BigDecimal gap = candle.getClose().subtract(candle.getLow());
        BigDecimal min = gap.multiply(minRate);
        BigDecimal random = BigDecimal.valueOf(new Random().nextDouble(gap.subtract(min).doubleValue()));
        return candle.getClose().subtract(min).subtract(random);
    }
}
