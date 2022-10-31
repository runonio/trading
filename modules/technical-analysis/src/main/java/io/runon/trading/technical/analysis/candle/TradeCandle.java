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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import io.runon.trading.BigDecimals;
import io.runon.trading.Trade;
import io.runon.trading.Volume;
import io.runon.trading.technical.analysis.volume.Volumes;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/**
 *거래 분석에 사용할 수 있는 캔들
 * 기본정보외에 분석에 필요한 거래정보 추가
 * @author macle
 */
@Slf4j
public class TradeCandle extends CandleStick implements Volume {

    public static final TradeCandle [] EMPTY_CANDLES = new TradeCandle[0];

    /**
     * 거래량
     */
    private BigDecimal volume = BigDecimal.ZERO;

    /**trading aomunt
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
     * 거래대금이 있을때는 거래대금 활용
     *  거래대금 없을떄는 대표가격이라고 보르는 (종가 + 고가 +저가 )/3
     *
     * @return  평균가격
     */
    public BigDecimal getAverage() {
        if(tradingPrice.compareTo(BigDecimal.ZERO) == 0){
            return close.add(high).add(low).divide(BigDecimals.DECIMAL_3, MathContext.DECIMAL128);
        }

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
        }else if(trade.getType() == Trade.Type.SELL){
            sellVolume = sellVolume.add(trade.getVolume());
            sellTradingPrice = sellTradingPrice.add(trade.getTradingPrice());
        }

        volume = volume.add(trade.getVolume());
        tradingPrice = tradingPrice.add(trade.getTradingPrice());
    }

    /**
     * 캔들정보추가 (정보합침)
     * @param addCandle 캔들정보를 활용하여 정보합침
     */
    public void addCandle(TradeCandle addCandle){
        if(openTime == -1 || openTime > addCandle.getOpenTime()) {
            openTime = addCandle.getOpenTime();
            open = addCandle.getOpen();
        }

        if(closeTime == -1 || closeTime < addCandle.getCloseTime()){
            closeTime = addCandle.getCloseTime();
            close = addCandle.getClose();
        }

        if(lastTradeTime < addCandle.getLastTradeTime()){
            //거래시간은 항상 최신값으로
            lastTradeTime = addCandle.getLastTradeTime();
            //총가는 항상 최신값으로
            if(addCandle.getClose() != null) {
                close = addCandle.getClose();
            }
        }

        low = BigDecimals.min(low, addCandle.getLow());
        high = BigDecimals.max(high, addCandle.getHigh());

        if(isTradeRecord && addCandle.getTradeList() != null){
            if(tradeList == null){
                tradeList = new ArrayList<>();
            }
            tradeList.addAll(addCandle.getTradeList());
        }

        tradeCount += addCandle.getTradeCount();

        addVolume(addCandle);
    }

    public void addVolume(TradeCandle addCandle){
        buyVolume = buyVolume.add(addCandle.getBuyVolume());
        buyTradingPrice = buyTradingPrice.add(addCandle.getBuyTradingPrice());

        sellVolume = sellVolume.add(addCandle.getSellVolume());
        sellTradingPrice = sellTradingPrice.add(addCandle.getSellTradingPrice());

        volume = volume.add(addCandle.getVolume());
        tradingPrice = tradingPrice.add(addCandle.getTradingPrice());
    }

    /**
     * 거래리스트 얻기
     * isTradeRecord = true 일때만 적용
     * @return 거래리스트
     */
    public List<Trade> getTradeList() {
        return tradeList;
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
            log.error("trade data not set");
            return;
        }

        for (Trade trade: tradeList) {
            addTrade(trade);
        }
    }

    private BigDecimal volumePower = null;

    /**
     * 체결강도 설정
     * @param volumePower 체결강도
     */
    public void setVolumePower(BigDecimal volumePower) {
        this.volumePower = volumePower;
    }

    /**
     * 체결강도 얻기
     * max  MAX_STRENGTH
     * @return  체결 강도
     */
    public BigDecimal getVolumePower(){

        if(isEndTrade && volumePower != null){
            return volumePower;
        }

        if((buyVolume == null ||  buyVolume.compareTo(BigDecimal.ZERO)==0) && (sellVolume == null ||  sellVolume.compareTo(BigDecimal.ZERO)==0) ){
            //구할 수 없음
            return null;
        }

        volumePower = Volumes.getVolumePower(buyVolume, sellVolume);
        return volumePower;
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
     * 거래대금이 설정된 경우 거래대금
     * 거래대금이 설정되지 않은경우 (종가 + 고가 + 저가 )/3 * 거래량
     * @return 거래대금
     */
    public BigDecimal getTradingPrice() {
        if(tradingPrice.compareTo(BigDecimal.ZERO) == 0 && volume.compareTo(BigDecimal.ZERO) > 0){
            return close.add(high).add(low).multiply(volume).divide(BigDecimals.DECIMAL_3, MathContext.DECIMAL128);
        }

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
     * 매도거래대금 설정
     * 매수대금을 활용한다
     */
    public void setSellTradingPrice() {
        if(tradingPrice == null || buyTradingPrice == null){
            return ;
        }

        this.sellTradingPrice = tradingPrice.subtract(buyTradingPrice);
    }

    /**
     * 최종거래시간 얻기
     * @return 최종거래시간
     */
    public long getLastTradeTime() {
        return lastTradeTime;
    }

    public void setLastTradeTime(long lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    public static TradeCandle sumCandles( TradeCandle [] candles , long openTime, long closeTime){
        TradeCandle tradeCandle = new TradeCandle();
        tradeCandle.addCandle(candles[candles.length-1]);
        for (int i = candles.length-2; i > -1 ; i--) {
            TradeCandle candle = candles[i];
            if(     //시작시간이 사이에 있거나
                    (openTime <= candle.getOpenTime() && closeTime >= candle.getOpenTime())
                    //시작시간이 걸쳐 있거나
                    || (candle.getOpenTime() <= openTime && candle.getCloseTime() > openTime)
                    //끝시간이 걸쳐 있거나
                    || (candle.getOpenTime() <= closeTime && candle.getCloseTime() > closeTime)
            ){
                tradeCandle.addCandle(candle);
            }else{
                break;
            }
        }
        tradeCandle.setPrevious(tradeCandle.getOpen());
        tradeCandle.setChange();
        return tradeCandle;
    }

    public void addTradingCount(int count){
        tradeCount += count;
    }

    @Override
    public String toString(){
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create().toJson(this);
    }
}