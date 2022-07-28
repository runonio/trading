package io.runon.trading.technical.analysis.indicator.nhnl;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.technical.analysis.SymbolCandle;
import io.runon.trading.technical.analysis.candle.Candles;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;

/**
 * 신고가 신저가 지수
 * New High New Low
 * @author macle
 */
public class Nhnl {

    private SymbolCandle[] symbolCandles;

    /**
     * 캔들 정보를 활용한 분석
     */
    public Nhnl(SymbolCandle[] symbolCandles){
        setSymbolCandles(symbolCandles, true);
    }

    private long candleTime = -1;
    private long searchTime = Times.DAY_1;
    private long timeRange = Times.WEEK_52;

    private BigDecimal minTradingPrice = null;

    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }

    public void setTimeRange(long timeRange) {
        this.timeRange = timeRange;
    }

    /**
     * 최소 가래대금 설정
     */
    public void setMinTradingPrice(BigDecimal minTradingPrice) {
        this.minTradingPrice = minTradingPrice;
    }

    private int validSymbolSize = 0;

    public int validSymbolSize(){
        return validSymbolSize;
    }

    public NhnlData analysis(){


        return null;
    }


    public BigDecimal getIndex(Candles[] candlesArray, long candleOpenTime, long candleTime){

        validSymbolSize = 0;

        long searchEndTime = candleOpenTime - searchTime + candleTime;
        long rangeEndTime = candleOpenTime - timeRange + candleTime;

        for(Candles candlesGet : candlesArray){
            TradeCandle [] candles = candlesGet.getCandles();
            
            int candleIndex = -1;
            for (int i = candles.length-1; i > -1; i--) {
                TradeCandle candle = candles[i];
                if(candle.getOpenTime() <= candleOpenTime){
                    candleIndex = i;
                    break;
                }
            }

            if(candleIndex == -1){
                continue;
            }

            //적어도 searchTime 의 2배 이상은 되어야함함
            long timeMax = (candleIndex +1) *candleTime;
            if(timeMax < searchTime*2){
                continue;
            }


            BigDecimal high = candles[candleIndex].getHigh();
            BigDecimal low = candles[candleIndex].getLow();

            for (int i = candleIndex-1; i > -1; i--) {

            }



        }

        return null;
    }

    public SymbolCandle[] getLowSymbolCandles(){
        return null;
    }

    public SymbolCandle[] getHighSymbolCandles(){
        return null;
    }

    public SymbolCandle[] getSymbolCandles() {
        return symbolCandles;
    }

    public void setSymbolCandles(SymbolCandle[] symbolCandles){
        setSymbolCandles(symbolCandles, true);
    }

    public void setSymbolCandles(SymbolCandle[] symbolCandles, boolean isCandleTimeUpdate) {
        this.symbolCandles = symbolCandles;

        if(isCandleTimeUpdate) {
            for (SymbolCandle symbolCandle : symbolCandles) {
                TradeCandle[] candles = symbolCandle.getCandles();
                if (candles.length > 2) {
                    break;
                }
                candleTime = candles[1].getTime() - candles[0].getTime();
            }
        }
    }


}
