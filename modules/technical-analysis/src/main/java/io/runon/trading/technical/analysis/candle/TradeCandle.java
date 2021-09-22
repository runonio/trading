/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.runon.trading.technical.analysis.candle;

import io.runon.trading.Trade;
import io.runon.trading.TradingBigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/**
 *거래 분석에 사용할 수 있는 캔들
 * 기본정보외에 분석에 필요한 거래정보 추가
 * @author macle
 */
public class TradeCandle extends CandleStick {

    private static final Logger logger = LoggerFactory.getLogger(TradeCandle.class);

    /**
     * 거래량
     */
    private BigDecimal volume = BigDecimal.ZERO;

    /**
     * 매수량
     */
    private BigDecimal buyVolume = BigDecimal.ZERO;

    /**
     * 매도량
     */
    private BigDecimal sellVolume = BigDecimal.ZERO;


    /**
     * 평균가격 얻기
     * @return  평균가격
     */
    public BigDecimal getAverage() {
        return priceTotal.divide(volume, MathContext.DECIMAL128);
    }


    /**
     * 거래량 얻기
     * @return  거래량
     */
    public BigDecimal getVolume() {
        return volume;
    }

    /**
     * 거래량 설정
     * @param volume  거래량
     */
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    /**
     * 거래 횟수(건수)
     * 한명이 대량 거래를 한건지를 측정하기위한 변수
     */
    private int tradeCount = 0;

    BigDecimal priceTotal = BigDecimal.ZERO;
    /**
     * 거래 정보 리스트
     */
    private List<Trade> tradeList = null;

    /**
     * 거래정보 추가
     * @param trade Trade 거래정보
     */
    public void addTrade(Trade trade){

        if(tradeList == null){
            tradeList = new ArrayList<>();
            setOpen(trade.getPrice());
            setClose(trade.getPrice());
            high = trade.getPrice();
            low = trade.getPrice();
        }else{

            if(high.compareTo(trade.getPrice()) < 0){
                high = trade.getPrice();
            } else if(low.compareTo(trade.getPrice()) > 0){
                low =  trade.getPrice();
            }
            setClose(trade.getPrice());
        }
        tradeList.add(trade);
        tradeCount = tradeList.size();

        if(trade.getType() == Trade.Type.BUY){
            buyVolume = buyVolume.add(trade.getVolume());
        }else{
            sellVolume = sellVolume.add(trade.getVolume());
        }

        volume = volume.add(trade.getVolume());
        priceTotal = priceTotal.add(trade.getVolume().multiply(trade.getPrice()));
    }

    /**
     * 거래 회수(건수) 얻기
     * 거래량과 다름 회수별 거래량이 존재
     * @return int 거래회수(건수)
     */
    public int getTradeCount() {
        return tradeCount;
    }

    /**
     * 거래정보를 이용하여 캔들 데이터 설정
     * trade 를 한번에 추가했을때 사용
     */
    public void setCandleToTrade(){
        if(tradeList == null  || tradeList.size() == 0){
            logger.error("trade data not set");
            return;
        }

        priceTotal = BigDecimal.ZERO;

        tradeCount = tradeList.size();


        Trade firstTrade = tradeList.get(0);
        Trade endTrade = tradeList.get(tradeList.size()-1);

        setOpen(firstTrade.getPrice());
        setClose(endTrade.getPrice());

        volume = BigDecimal.ZERO;
        BigDecimal high = firstTrade.getPrice();
        BigDecimal low = firstTrade.getPrice();

        for (Trade trade: tradeList) {

            volume = volume.add(trade.getVolume());
            priceTotal = priceTotal.add(trade.getVolume().multiply(trade.getPrice()));

            if(high.compareTo(trade.getPrice()) < 0){
                high = trade.getPrice();
            }

            if(low.compareTo(trade.getPrice()) > 0){
                low =  trade.getPrice();
            }

            if(trade.getType() == Trade.Type.BUY){
                buyVolume = buyVolume.add(trade.getVolume());
            }else{
                sellVolume = sellVolume.add(trade.getVolume());
            }
        }
        setHigh(high);
        setLow(low);
    }

    //100.0 == 100% , 500.0 == 500%
    public static final BigDecimal MAX_STRENGTH = new BigDecimal(500);


    private BigDecimal strength = null;

    /**
     * 체결강도 설정
     * @param strength 체결강도
     */
    public void setStrength(BigDecimal strength) {
        this.strength = strength;
    }

    /**
     * 체결강도 얻기
     * max  MAX_STRENGTH
     * @return  체결 강도
     */
    public BigDecimal strength(){

        if(isEndTrade && strength != null){
            return strength;
        }

        if(sellVolume == null && buyVolume == null){
            return BigDecimal.ONE;
        }

        if(sellVolume == null || sellVolume.compareTo(BigDecimal.ZERO) == 0){
            //500%
            return MAX_STRENGTH;
        }

        BigDecimal strength = buyVolume.divide(sellVolume, MathContext.DECIMAL128).multiply(TradingBigDecimal.DECIMAL_100);

        if(strength.compareTo(MAX_STRENGTH) > 0){
            this.strength = MAX_STRENGTH;
            return MAX_STRENGTH;
        }

        this.strength = strength;
        return strength;
    }

    /**
     * 거래정보 초기화
     * 메모리 관리용 메소드
     */
    public void clearTrade(){
        if(tradeList == null){
            return;
        }

        tradeList.clear();
        tradeList = null;
    }

    /**
     * 거래회수 설정
     * 거래량과 다름 회수별 거래량이 존재
     * @param tradeCount int 거래회수(건수)
     */
    public void setTradeCount(int tradeCount) {
        this.tradeCount = tradeCount;
    }


}