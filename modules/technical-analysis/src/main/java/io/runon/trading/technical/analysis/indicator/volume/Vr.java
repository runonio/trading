package io.runon.trading.technical.analysis.indicator.volume;

import com.seomse.commons.config.Config;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
/**
 * Volume Ratio
 *  ( 주가상승일 거래량 / 주가하락일 거래량) * 100%
 *
 *  m.blog.naver.com/myungli/221964994908
 * @author macle
 */
public class Vr {

    public static final BigDecimal MAX_VOLUME_RATIO = new BigDecimal(Config.getConfig("max.volume.ratio", "500"));

    public static BigDecimal get(TradeCandle[] array, int n){

        BigDecimal up = BigDecimal.ZERO;


        return null;
    }

}
