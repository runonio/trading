package io.runon.trading.data;

import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
public class VolumeAmountBuySell {
    BigDecimal buyVolume;
    BigDecimal sellVolume;

    BigDecimal buyAmount;
    BigDecimal sellAmount;


    /**
     * 순매수 거래량얻기
     * @return 순매수 거래량
     */
    public BigDecimal getNetBuyVolume(){
        return buyVolume.subtract(sellVolume);
    }

    /**
     * 순매수 거래대금 얻기
     * @return 순매수 거래대금
     */
    public BigDecimal getNetBuyAmount(){
        return buyAmount.subtract(sellAmount);
    }

    public BigDecimal getVolume(){
        return buyVolume.add(sellVolume);
    }

    public BigDecimal getAmount(){
        return buyAmount.add(sellAmount);
    }

    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }
}
