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
package io.runon.trading.technical.analysis.candle.candles;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.Trade;
import io.runon.trading.TradeAdd;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * 여러개의 TradeCandle 정보
 * @author macle
 */
public class TradeCandles {

    private static final Logger logger = LoggerFactory.getLogger(TradeCandles.class);

    public static final int DEFAULT_COUNT = 1000;

    private static final CandleChangeObserver[] EMPTY_OBSERVER = new CandleChangeObserver[0];

    public static final TradeCandle[] EMPTY_CANDLES = new TradeCandle[0];

    //24시간으로 나눌 수 있는 값만 설정 가능
    private final long timeGap ;

    private int count = DEFAULT_COUNT;

    TradeAdd tradeAdd ;

    List<TradeCandle> candleList = new LinkedList<>();
    TradeCandle[] candles = EMPTY_CANDLES;

    TradeCandle lastCandle = null;
    BigDecimal shortGapRatio ;
    BigDecimal steadyGapRatio ;

    boolean isEmptyCandleContinue = false;

    private final Object observerLock = new Object();
    private CandleChangeObserver[] observers = EMPTY_OBSERVER;

    private final List<CandleChangeObserver> observerList = new LinkedList<>();

    private boolean isTradeRecord = false;

    private long lastTime = System.currentTimeMillis();

    private boolean isValidTime = false;

    /**
     * 캔들 유효시간 사용여부 설정
     * 최신시간에서 유효시간을 체크하여 유요하지 않으시간캔들 삭제
     * @param isValidTime 캔들 유효시간 사용여부
     */
    public void setIsValidTime(boolean isValidTime) {
        this.isValidTime = isValidTime;
    }

    /**
     * 거래정보 기록 여부 설정
     * 설정하지 않으면 false
     * @param tradeRecord 거래정보 기록 여부
     */
    public void setTradeRecord(boolean tradeRecord) {
        isTradeRecord = tradeRecord;
    }

    /**
     *  캔들 변화 인지 옵져버 추가
     * @param candleChangeObserver CandleChangeObserver  candle change observer
     */
    public void addChangeObserver(CandleChangeObserver candleChangeObserver){
        synchronized (observerLock) {
            if (observerList.contains(candleChangeObserver)) {
                return;
            }
            observerList.add(candleChangeObserver);
            observers = observerList.toArray(new CandleChangeObserver[0]);
        }
    }

    /**
     * 캔들 배열의 유형을 설정
     * shortGapPercent
     * steadyGapPercent
     * 설정하고 실행하여야 한다.
     */
    public void setCandleType(){
        if(steadyGapRatio == null || shortGapRatio == null){
            logger.error("shortGapPercent, steadyGapPercent set: " + shortGapRatio +", " + steadyGapRatio);
            return;
        }

        TradeCandle[] candles = this.candles;
        for(TradeCandle candle : candles){
            candle.setType(shortGapRatio, steadyGapRatio);
        }

    }

    /**
     * 캔들 변화 인지 옵저버 제거
     * @param candleChangeObserver CandleChangeObserver candle change observer
     */
    public void removeObserver(CandleChangeObserver candleChangeObserver){
        synchronized (observerLock) {
            if (!observerList.contains(candleChangeObserver)) {
                return;
            }
            observerList.remove(candleChangeObserver);
            observers = observerList.toArray(new CandleChangeObserver[0]);
        }
    }

    /**
     * 생성자
     * @param timeGap long timeGap
     */
    public TradeCandles(long timeGap ){
        //24시간 이하의 값에서는 24보다 낮은 값만 구할 수 있음
        if(timeGap < Times.DAY_1 &&
                Times.DAY_1%timeGap != 0){
            throw new RuntimeException("24 hour % timeGap 0: "  +  Times.DAY_1%timeGap );
        }
        this.timeGap = timeGap;
        tradeAdd = new FirstTradeAdd(this);
    }

    /**
     * 빈켄들 정보로 이어지게 할지 여부 설정
     *@param emptyCandleContinue boolean isEmptyCandleContinue
     */
    public void setEmptyCandleContinue(boolean emptyCandleContinue) {
        isEmptyCandleContinue = emptyCandleContinue;
    }

    /**
     * 생성자
     * 처음부터 많은 켄들이 한번에 추가될 경우
     * @param timeGap long timeGap
     * @param candles TradeCandle ready candles
     * @param saveCount int save count
     */
    public TradeCandles(long timeGap, TradeCandle[] candles, int saveCount ){
        if(timeGap < Times.DAY_1 &&
                Times.DAY_1%timeGap != 0){
            throw new RuntimeException("24 hour % timeGap 0: "  +  Times.DAY_1%timeGap );
        }

        this.timeGap = timeGap;
        this.count = saveCount;


        if(candles == null || candles.length == 0){
            tradeAdd = new FirstTradeAdd(this);
            return;
        }

        lastCandle = candles[candles.length-1];


        tradeAdd = new NextTradeAdd(this);
        if(candles.length <= saveCount) {
            candleList.addAll(Arrays.asList(candles));
            this.candles = candles;
        }else{

            //noinspection ManualArrayToCollectionCopy
            for (int i = candles.length - saveCount ; i < candles.length; i++) {
                candleList.add(candles[i]);
            }
            this.candles = candleList.toArray(new TradeCandle[0]);
        }
    }


    /**
     * add trade candles
     * @param tradeCandles TradeCandle []
     */
    public void addCandle(TradeCandle[] tradeCandles){
        for(TradeCandle tradeCandle : tradeCandles){
            addCandle(tradeCandle, false);
        }
        this.candles = candleList.toArray(new TradeCandle[0]);
    }


    /**
     * add candle
     * @param tradeCandle TradeCandle add trade candle
     */
    public void addCandle(TradeCandle tradeCandle){
        addCandle(tradeCandle, true);
    }

    /**
     * add candle
     * @param tradeCandle TradeCandle add trade candle
     * @param isCandlesChange boolean candles array change flag
     */
    public void addCandle(TradeCandle tradeCandle, boolean isCandlesChange){
        lastTime = System.currentTimeMillis();
        tradeCandle.setTradeRecord(isTradeRecord);
        TradeCandle lastEndCandle = null;


        if(lastCandle != null && lastCandle.getOpenTime() == tradeCandle.getOpenTime()){
            //마지막 캔들 교체

            candleList.remove(candleList.size()-1);

        }else {
            if (candles.length > 0) {

                lastEndCandle = candles[candles.length - 1];

                //마지막 캔들이 더이상 변화가 없는상태로 변환
                lastEndCandle.setEndTrade();

                //캔들유형을
                if (shortGapRatio != null && steadyGapRatio != null && lastEndCandle.getType() == CandleStick.Type.UNDEFINED) {
                    lastEndCandle.setType(lastEndCandle.getOpen().multiply(shortGapRatio), lastEndCandle.getOpen().multiply(steadyGapRatio));
                }

                //새로 들어온 캔들이 변화가 없는 캔들일 경우
                if (tradeCandle.isEndTrade()) {
                    if (shortGapRatio != null && steadyGapRatio != null && tradeCandle.getType() == CandleStick.Type.UNDEFINED) {
                        tradeCandle.setType(tradeCandle.getOpen().multiply(shortGapRatio), tradeCandle.getOpen().multiply(steadyGapRatio));
                    }
                    lastEndCandle = tradeCandle;
                }
            }

        }

        candleList.add(tradeCandle);

        for(;;){
            if(candleList.size() > count){
                candleList.remove(0);
            }else{
                break;
            }
        }

        if(isValidTime && candleList.size() > 1){
            long validTime = tradeCandle.getOpenTime() - (timeGap*count);

            for(;;){
                if(candleList.size() <2){
                    break;
                }

                TradeCandle candle = candleList.get(0);
                if(candle.getOpenTime() < validTime){
                    candleList.remove(0);
                }else{
                    break;
                }
            }

        }

        if(isCandlesChange) {
            this.candles = candleList.toArray(new TradeCandle[0]);
        }
        lastCandle = tradeCandle;

        CandleChangeObserver[] observers = this.observers;

        for(CandleChangeObserver observer : observers){
            observer.changeCandle(lastEndCandle, tradeCandle);
        }
    }


    /**
     * 거래정보 추가
     * trade add
     * @param trade 거래정보
     */
    public void addTrade(Trade trade){
        lastTime = System.currentTimeMillis();
        tradeAdd.addTrade(trade);
    }

    /**
     * 타임 갭 얻기
     * timeGap get
     * @return long timeGap
     */
    public long getTimeGap() {
        return timeGap;
    }

    /**
     * 캔들 저장 count 설정
     * candle count set
     * @param count int candle  count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 캔들을 추가로 생성하여 트레이드 정보 입력
     * @param trade 거래정보
     * @param startTime long startTime
     * @param endTime long endTime
     */
    void addTradeNewCandle(Trade trade, long startTime, long endTime){
        lastTime = System.currentTimeMillis();
        TradeCandle tradeCandle = new TradeCandle();
        tradeCandle.setOpenTime(startTime);
        tradeCandle.setCloseTime(endTime);
        tradeCandle.addTrade(trade);
        addCandle(tradeCandle);
    }

    /**
     * 설정된 캔들 저장 건수 얻기
     * candle count get
     * @return int candle count
     */
    public int getCount() {
        return count;
    }

    /**
     * 길어얻기
     * candles length
     * @return int candles length
     */
    public int length(){
        return candles.length;
    }

    /**
     * 캔들 배열 얻기
     * candles get
     * @return TradeCandle candles
     */
    public TradeCandle[] getCandles() {
        return candles;
    }

    /**
     * 짧은캔들 기준 변화률 설정
     * 시작기 기준의 비율
     * @param shortGapRatio  짧은 캔들 기준 변화률
     */
    public void setShortGapRatio(BigDecimal shortGapRatio) {
        this.shortGapRatio = shortGapRatio;
    }

    /**
     * 보합 기준 변화률 설정
     * 시작가 기준의 비율
     * @param steadyGapRatio  보합 기준 변화률
     */
    public void setSteadyGapRatio(BigDecimal steadyGapRatio) {
        this.steadyGapRatio = steadyGapRatio;
    }

    /**
     * 짧은캔들 gap percent
     * @return  shot gap percent
     */
    public BigDecimal getShortGapRatio() {
        return shortGapRatio;
    }

    /**
     * 보합 gap percent
     * @return  steady gap percent
     */
    public BigDecimal getSteadyGapRatio() {
        return steadyGapRatio;
    }

    /**
     * 최종 추가 시간 얻기
     * @return 최종 추가 시간
     */
    public long getLastTime() {
        return lastTime;
    }


    // 주식쪽에 옮겨갈 소스로 옮기기 전까지는 주석처리
//    /**
//     * 빈 캔들을 채운다.
//     * @param businessDayList 정렬 된 영업일 목록 yyyyMMdd
//     */
//    public void makeEmptyCandle(List<String> businessDayList){
//        boolean lessThenDay = timeGap < Times.DAY_1;
//        int candleSize = candleList.size();
//        List<TradeCandle> newCandleList = new ArrayList<>();
//        TradeCandle tradeCandle = null;
//        boolean isNewCandleMake = false;
//        for (int i = 0; i < candleSize-1; i++) {
//            if(!isNewCandleMake){
//                tradeCandle = candleList.get(i);
//            }
//            newCandleList.add(tradeCandle);
//            TradeCandle nextTradeCandle = candleList.get(i+1);
//            long tradeOpenTime = tradeCandle.getOpenTime();
//            long nextTradeOpenTime = nextTradeCandle.getOpenTime();
//            if(tradeOpenTime + timeGap >= nextTradeOpenTime){
//                isNewCandleMake = false;
//                continue;
//            }
//
//            String tradeOpenYmd = DateUtil.getDateYmd(tradeOpenTime,"yyyyMMdd");
//            String nextTradeOpenYmd = getNextOpenDay(tradeOpenYmd,businessDayList);
//            if(nextTradeOpenYmd == null){
//                isNewCandleMake = false;
//                continue;
//            }
//            long nextOpenTime = getNextOpenTime(tradeOpenTime,nextTradeOpenYmd,lessThenDay);
//            if(waitingTime(nextOpenTime , lessThenDay) ||
//                    nextOpenTime >= nextTradeOpenTime){
//                isNewCandleMake = false;
//                continue;
//            }
//            BigDecimal close = tradeCandle.getClose();
//            TradeCandle newCandle = new TradeCandle();
//            newCandle.setLow(close);
//            newCandle.setHigh(close);
//            newCandle.setOpen(close);
//            newCandle.setClose(close);
//            newCandle.setPrevious(close);
//            newCandle.setOpenTime(nextOpenTime);
//            newCandle.setCloseTime(nextOpenTime + timeGap);
////            logger.debug("newCandle set " + DateUtil.getDateYmd(nextOpenTime,"yyyy-MM-dd HH:mm:ss"));
//            i--;
//            tradeCandle = newCandle;
//            isNewCandleMake = true;
//        }
//
//        newCandleList.add(candleList.get(candleSize-1));
//
//        logger.debug(candleList.size() + " --> " +newCandleList.size());
//
//        candles = new TradeCandle[newCandleList.size()];
//        candles = newCandleList.toArray(candles);
//        candleList = newCandleList;
//        count = newCandleList.size();
//        isEmptyCandleMake = true;
//    }
//    /**
//     * 영업 대기 시간 체크
//     * @param nextOpenTime 다음 시작 시간
//     * @param lessThenDay 일일 데이터 보다 작은지 여부
//     * @return boolean
//     */
//    private boolean waitingTime(long nextOpenTime, boolean lessThenDay) {
//        int hm = Integer.parseInt( DateUtil.getDateYmd(nextOpenTime,"HHmm") );
//        if(lessThenDay){
//            if(hm >= 1520){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 다음 시작일을 얻는다
//     * @param tradeOpenYmd String
//     * @param businessDayList 영업일 목록
//     * @return
//     */
//    private String getNextOpenDay(String tradeOpenYmd , List<String> businessDayList) {
//        int businessDaySize = businessDayList.size();
//        int findIndex = -1;
//        String result = null;
//        for (int i = 0; i <businessDaySize-1; i++) {
//            String businessDay = businessDayList.get(i);
//            String nextBusinessDay = businessDayList.get(i+1);
//            if(!businessDay.equals(tradeOpenYmd)){
//                continue;
//            } else {
//                findIndex = i;
//                result = nextBusinessDay;
//                break;
//            }
//        }
//        for (int i = 0; i < findIndex; i++) {
//            businessDayList.remove(0);
//        }
//        return result;
//    }
//
//    /**
//     * 다음 시작 시간을 얻는다
//     * @param tradeOpenTime long
//     * @param nextTradeOpenYmd String
//     * @param lessThenDay boolean
//     * @return
//     */
//    private long getNextOpenTime(long tradeOpenTime,String nextTradeOpenYmd,boolean lessThenDay) {
//        long nextTime = tradeOpenTime + timeGap;
//        int nextTimeHm = Integer.parseInt( DateUtil.getDateYmd(nextTime,"HHmm") );
//        if(lessThenDay){
//            if(nextTimeHm > 1535){ // 15:35
//                return DateUtil.getDateTime( nextTradeOpenYmd + "0900" , "yyyyMMddHHmm");
//            } else {
//                return nextTime;
//            }
//        } else {
//            return DateUtil.getDateTime(nextTradeOpenYmd, "yyyyMMdd");
//        }
//    }
//
//    /**
//     * 현재 정보로 더 큰 시간의 TradeCandles 를 만든다.
//     * @param bigTimeGap 더 큰 시간 갭
//     * @return BigTime TradeCandle
//     */
//    public TradeCandles getBigTimeTradeCandles(long bigTimeGap){
//        if(bigTimeGap <= timeGap){
//            logger.error("time setting error : [" + bigTimeGap + "] is bigger than [" + timeGap + "]");
//            return null;
//        }
//        if(!isEmptyCandleMake){
//            logger.error("empty candle make first : makeEmptyCandle(List<String> businessDayList);");
//            return null;
//        }
//        TradeCandles bigTradeCandles = new TradeCandles(bigTimeGap);
//        bigTradeCandles.setShortGapRatio(this.shortGapRatio);
//        bigTradeCandles.setSteadyGapRatio(this.steadyGapRatio);
//
//        int candleSize = candleList.size();
//
//        List<TradeCandle> bigTradeCandleList = new ArrayList<>();
//        BigDecimal previous = -1.0;
//        if(candleList.size() > 0 ) {
//            previous = candleList.get(0).getPrevious();
//        }
//        for (int i = 0; i < candleSize; i++) {
//
//            TradeCandle bigTradeCandle = new TradeCandle();
//
//            TradeCandle tradeCandle = candleList.get(i);
//            int tradeCount = tradeCandle.getTradeCount();
//            BigDecimal volume = tradeCandle.getVolume();
//            BigDecimal low = tradeCandle.getLow();
//            BigDecimal high = tradeCandle.getHigh();
//            BigDecimal open = tradeCandle.getOpen();
//            BigDecimal close = tradeCandle.getClose();
//            long openTime = tradeCandle.getOpenTime();
//            long closeTime = tradeCandle.getCloseTime();
//            for (int j = i+1; j < candleSize ; j++) {
//                TradeCandle nextTradeCandle = candleList.get(j);
//                int nextTradeCount = nextTradeCandle.getTradeCount();
//                BigDecimal nextVolume = nextTradeCandle.getVolume();
//                BigDecimal nextLow = nextTradeCandle.getLow();
//                BigDecimal nextHigh = nextTradeCandle.getHigh();
//                BigDecimal nextClose = nextTradeCandle.getClose();
//                long nextOpenTime = nextTradeCandle.getOpenTime();
//                BigDecimal nextAverage = tradeCandle.getAverage();
//
//                if(nextOpenTime >= openTime + bigTimeGap ){
//                    break;
//                }
//
//                volume += nextVolume;
//                if(nextLow < low){
//                    low = nextLow;
//                }
//                if(nextHigh > high){
//                    high = nextHigh;
//                }
//                close = nextClose;
//                tradeCount += nextTradeCount;
//                i++;
//            }
//            bigTradeCandle.setTradeCount(tradeCount);
//            bigTradeCandle.setOpen(open);
//            bigTradeCandle.setClose(close);
//            bigTradeCandle.setHigh(high);
//            bigTradeCandle.setLow(low);
//            bigTradeCandle.setOpenTime(openTime);
//            bigTradeCandle.setCloseTime(closeTime);
//            bigTradeCandle.setPrevious(previous);
//            bigTradeCandle.setVolume(volume);
//            bigTradeCandleList.add(bigTradeCandle);
//            previous = close;
//        }
//        TradeCandle[] bigTradeCandleArr = new TradeCandle[bigTradeCandleList.size()];
//        bigTradeCandleArr = bigTradeCandleList.toArray(bigTradeCandleArr);
//        return new TradeCandles(bigTimeGap,bigTradeCandleArr,bigTradeCandleArr.length);
//    }
}