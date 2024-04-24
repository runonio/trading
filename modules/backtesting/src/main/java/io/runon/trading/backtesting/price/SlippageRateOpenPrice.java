package io.runon.trading.backtesting.price;

import io.runon.trading.Candle;

import java.math.BigDecimal;

/**
 * 슬리피지를 특정 비율로만 계산할때
 * 시가 활용
 * 이전 캔들을 보고 바로 다음 시가에 들어간다고 가정한 방식
 * 1분봉 분석시 다음 시가가 슬리피지가 적음
 * (이전종가와 비슷한 가격에 선택될 확율이 높음)
 * @author macle
 */
public class SlippageRateOpenPrice extends CandleMapPrice {

    //기본값 0.25%
    private BigDecimal rate = new BigDecimal("0.0025");

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal getBuyPrice(String id) {
        Candle candle = candleMap.get(id);
        BigDecimal price = candle.getOpen();

        if(rate.compareTo(BigDecimal.ZERO) == 0){
            return price;
        }
        return price.add(price.multiply(rate));
    }

    @Override
    public BigDecimal getSellPrice(String id) {

        Candle candle = candleMap.get(id);
        BigDecimal price = candle.getOpen();
        if(rate.compareTo(BigDecimal.ZERO) == 0){
            return price;
        }

        return price.subtract(price.multiply(rate));
    }
}
