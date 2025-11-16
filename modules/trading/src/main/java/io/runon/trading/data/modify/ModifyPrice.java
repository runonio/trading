package io.runon.trading.data.modify;

import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.technical.analysis.candle.CandlePreviousCandle;
import io.runon.trading.technical.analysis.candle.TradeCandle;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 수정주가 검색
 * 데이터 검증
 * 수정주가 사유가 발생한경우 전체 캔들을 다시 받아야함
 * @author macle
 */
public class ModifyPrice {


    /**
     * 2000 년 이전데이터는 수정주가가 잘 반영되지 않은 정보가 있음.
     * 값이 1원의 오차율이 자주발생함
     * LockType 을 활용한 수정주가 찾기
     */
    public static List<CandlePreviousCandle> search(String dirPath, long candleTime, BigDecimal errorRate, BigDecimal errorPrice, long beginTime){
        //락 유형을 활용한 수정주가 분석
        TradeCandle [] candles =  CsvCandle.load(dirPath, candleTime);

        List<CandlePreviousCandle> list = null;

//        System.out.println(candles.length);

        for (int i = 1; i < candles.length; i++) {
            TradeCandle candle = candles[i];

            if(candle.getOpenTime() < beginTime){
                continue;
            }
//
//            String ymd = YmdUtils.getYmd(candle.getOpenTime(), TradingTimes.KOR_ZONE_ID);
//            if(ymd.startsWith("20240709")){
//                BigDecimal previous = candle.getPrevious();
//                TradeCandle previousCandle = candles[i-1];
//
//                System.out.println(ymd +", " + candle.getClose().toPlainString() +"," + previous.toPlainString() + ", " + previousCandle.getClose().toPlainString());
//            }else{
//                continue;
//            }

            BigDecimal previous = candle.getPrevious();
            TradeCandle previousCandle = candles[i-1];

            if(previous.compareTo(previousCandle.getClose()) == 0){
                continue;
            }


            if(previous.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }

            BigDecimal previousAbs = previous.abs();

            BigDecimal gapPrice = previous.subtract(previousCandle.getClose()).abs();

            if(gapPrice.compareTo(errorPrice) <= 0){
                continue;
            }


            if(errorRate == null){
                if(list == null){
                    list = new ArrayList<>();
                }
                list.add(new CandlePreviousCandle(candle, previousCandle));
            }else{
                gapPrice = gapPrice.abs();

                BigDecimal rate = gapPrice.divide(previousAbs, MathContext.DECIMAL128);
                if(errorRate.compareTo(rate) < 0){
                    if(list == null){
                        list = new ArrayList<>();
                    }
                    list.add(new CandlePreviousCandle(candle, previousCandle));
                }
            }
        }

        if(list == null){
            return Collections.emptyList();
        }

        return list;
    }





}