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
     * 거래대금
     */
    private BigDecimal tradingPrice = BigDecimal.ZERO;

    /**
     * 매수 거래대금
     */
    private BigDecimal buyTradingPrice = BigDecimal.ZERO;

    /**
     * 매도 거래대금
     */
    private BigDecimal sellTradingPrice = BigDecimal.ZERO;


    /**
     * 평균가격 얻기
     * @return  평균가격
     */
    public BigDecimal getAverage() {
        return tradingPrice.divide(volume, MathContext.DECIMAL128);
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


    /**
     * 거래 정보 리스트
     */
    private List<Trade> tradeList = null;

    private boolean isTradeRecord = false;

    /**
     * 거래정보 기록 여부 설정
     * 설정하지 않으면 false
     * @param tradeRecord 거래정보 기록 여부
     */
    public void setTradeRecord(boolean tradeRecord) {
        isTradeRecord = tradeRecord;
    }


    private boolean isInit = false;

    private long lastTradeTime = System.currentTimeMillis();

    /**
     * 거래정보 추가
     * @param trade Trade 거래정보
     */
    public void addTrade(Trade trade){

        lastTradeTime = trade.getTime();

        if(!isInit){
            isInit = true;

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
        if(isTradeRecord){
            if(tradeList == null){
                tradeList = new ArrayList<>();
            }
            tradeCount = tradeList.size();
            tradeList.add(trade);
        }else{
            tradeCount++;
        }

        if(trade.getType() == Trade.Type.BUY){
            buyVolume = buyVolume.add(trade.getVolume());
            buyTradingPrice = buyTradingPrice.add(trade.getTradingPrice());
        }else{
            sellVolume = sellVolume.add(trade.getVolume());
            sellTradingPrice = sellTradingPrice.add(trade.getTradingPrice());
        }

        volume = volume.add(trade.getVolume());
        tradingPrice = tradingPrice.add(trade.getTradingPrice());
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

        for (Trade trade: tradeList) {
            addTrade(trade);
        }

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


    /**
     * 매수 거래량 얻기
     * @return 매수 거래량
     */
    public BigDecimal getBuyVolume() {
        return buyVolume;
    }

    /**
     * 매도 거래량 얻기
     * @return 매도거래량
     */
    public BigDecimal getSellVolume() {
        return sellVolume;
    }

    /**
     * 거래대금 얻기
     * @return 거래대금
     */
    public BigDecimal getTradingPrice() {
        return tradingPrice;
    }

    /**
     * 매수거래대금 얻기
     * @return 매수거래대금
     */
    public BigDecimal getBuyTradingPrice() {
        return buyTradingPrice;
    }

    /**
     * 매도거래대금 얻기
     * @return 매도거래대금
     */
    public BigDecimal getSellTradingPrice() {
        return sellTradingPrice;
    }

    /**
     * 거래대금 설정
     * @param tradingPrice 거래대금
     */
    public void setTradingPrice(BigDecimal tradingPrice) {
        this.tradingPrice = tradingPrice;
    }

    /**
     * 매수볼륨 설정
     * @param buyVolume 매수 볼륨
     */
    public void setBuyVolume(BigDecimal buyVolume) {
        this.buyVolume = buyVolume;
    }

    /**
     * 메도볼륨 설정
     * @param sellVolume 매도 볼륨
     */
    public void setSellVolume(BigDecimal sellVolume) {
        this.sellVolume = sellVolume;
    }

    /**
     * 매도볼륨 설정
     * 매수 볼륨을 활용해서 설정한다.
     */
    public void setSellVolume(){
        if(buyVolume == null || volume == null){
            return ;
        }
        sellVolume = volume.subtract(buyVolume);
    }

    /**
     * 매수거래대금 설정
     * @param buyTradingPrice 매수거래대금
     */
    public void setBuyTradingPrice(BigDecimal buyTradingPrice) {
        this.buyTradingPrice = buyTradingPrice;
    }

    /**
     * 매도거래대금 설정
     * @param sellTradingPrice 매도거래대금
     */
    public void setSellTradingPrice(BigDecimal sellTradingPrice) {
        this.sellTradingPrice = sellTradingPrice;
    }

    /**
     * 최종거래시간 얻기
     * @return 최종거래시간
     */
    public long getTradeLastTime() {
        return lastTradeTime;
    }
}