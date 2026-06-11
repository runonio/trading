
package io.runon.trading.technical.analysis.candle;

import io.runon.commons.utils.time.Times;
import io.runon.trading.Trade;
import io.runon.trading.TradingTimes;
 import io.runon.trading.technical.analysis.candle.CandleChangeObserver;
 import io.runon.trading.technical.analysis.candle.CandlesGet;
 import io.runon.trading.technical.analysis.candle.TradeCandle;
 import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 여러개의 TradeCandle 정보
 * @author macle
 */
@Slf4j
@SuppressWarnings("ConstantForZeroLengthArrayAllocation")
public class TradeCandlesGet implements CandlesGet {


    public static final int DEFAULT_COUNT = 1000;

    private static final CandleChangeObserver[] EMPTY_OBSERVER = new CandleChangeObserver[0];

    public static final TradeCandle[] EMPTY_CANDLES = new TradeCandle[0];

    //24시간으로 나눌 수 있는 값만 설정 가능
    private final long candleTime;

    private int count = DEFAULT_COUNT;

    TradeAdd tradeAdd ;


    List<TradeCandle> candleList = new LinkedList<>();

    TradeCandle[] candles = EMPTY_CANDLES;

    TradeCandle lastCandle = null;

    boolean isEmptyCandleContinue = false;

    private final Object observerLock = new Object();

    private boolean isTradeRecord = false;

    private long lastTime = System.currentTimeMillis();

    private boolean isValidTime = false;

    /**
     * 캔들 유효시간 사용여부 설정
     * 최신시간에서 유효시간을 체크하여 유요하지 않으시간캔들 삭제
     * @param isValidTime 캔들 유효시간 사용여부
     */
    public void setValidTime(boolean isValidTime) {
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



    private final String interval;
    /**
     * 생성자
     * @param candleTime long timeGap
     */
    public TradeCandlesGet(long candleTime ){
        //24시간 이하의 값에서는 24보다 낮은 값만 구할 수 있음
        if(candleTime < Times.DAY_1 &&
                Times.DAY_1%candleTime != 0){
            throw new RuntimeException("24 hour % timeGap 0: "  +  Times.DAY_1%candleTime );
        }
        this.candleTime = candleTime;
        interval = TradingTimes.getInterval(candleTime);
        tradeAdd = new FirstTradeAdd(this);
    }

    public TradeCandlesGet(String interval){
        this.interval = interval;
        this.candleTime = TradingTimes.getIntervalTime(interval);
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
     * @param candleTime long timeGap
     * @param candles TradeCandle ready candles
     * @param saveCount int save count
     */
    public TradeCandlesGet(long candleTime, TradeCandle[] candles, int saveCount ){
        if(candleTime < Times.DAY_1 &&
                Times.DAY_1%candleTime != 0){
            throw new RuntimeException("24 hour % timeGap 0: "  +  Times.DAY_1%candleTime );
        }

        this.candleTime = candleTime;
        this.count = saveCount;
        interval = TradingTimes.getInterval(candleTime);

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
     * 캔들 추가가 없는 벡테스티용
     * @param candles 캔들세팅
     */
    public void setCandles(TradeCandle [] candles){
        this.candles = candles;
        lastCandle = candles[candles.length-1];
    }

    public String getInterval(){
        return interval;
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
        setEnd();
    }

    /**
     * 캔들의 종료여부 설정
     */
    public void setEnd(){
        TradeCandle [] candles = this.candles;
        for (int i = 0; i < candles.length-2 ; i++) {
            candles[i].setEndTrade();
        }

        TradeCandle last = candles[candles.length-1];
        if(last.getCloseTime() <= System.currentTimeMillis()){
            last.setEndTrade();
        }
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
        TradeCandle lastEndCandle;


        if(lastCandle != null && lastCandle.getOpenTime() == tradeCandle.getOpenTime()){
            //마지막 캔들 교체

            candleList.remove(candleList.size()-1);
            candleList.add(tradeCandle);

            if(candles.length > 0 && candles[candles.length-1].getOpenTime() == tradeCandle.getOpenTime()){
                candles[candles.length-1] = tradeCandle;
            }else{
                if(isCandlesChange) {
                    this.candles = candleList.toArray(new TradeCandle[0]);
                }
            }
            lastCandle = tradeCandle;

            return;
        }else {
            if (candles.length > 0 && isCandlesChange) {

                lastEndCandle = candles[candles.length - 1];

                //마지막 캔들이 더이상 변화가 없는상태로 변환
                lastEndCandle.setEndTrade();


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
            long validTime = tradeCandle.getOpenTime() - (candleTime *count);

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

    }

    /**
     * 거래정보 추가
     * trade add
     * @param trade 거래정보
     */
    public TradeCandle addTrade(Trade trade){
        lastTime = System.currentTimeMillis();
        return tradeAdd.addTrade(trade);
    }

    public TradeCandle addTrade(Trade [] trades){
        lastTime = System.currentTimeMillis();
        TradeCandle last = null;
        for(Trade trade:  trades){
            last = tradeAdd.addTrade(trade);
        }
        return last;
    }

    /**
     * 1분봉 60000 (mills second)
     * timeGap get
     * @return long timeGap
     */
    public long getCandleTime() {
        return candleTime;
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
    TradeCandle addTradeNewCandle(Trade trade, long startTime, long endTime){
        lastTime = System.currentTimeMillis();
        TradeCandle tradeCandle = new TradeCandle();
        tradeCandle.setOpenTime(startTime);
        tradeCandle.setCloseTime(endTime);
        tradeCandle.addTrade(trade);
        addCandle(tradeCandle);
        return tradeCandle;
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
    @Override
    public TradeCandle[] getCandles() {
        return candles;
    }


    /**
     * 캔들 배열 얻기
     * candles get
     * @return TradeCandle candles
     */
    public TradeCandle[] getCandles(int count) {
        TradeCandle[] candles = this.candles;
        return getCandles(candles, candles.length-1, count);
    }

    /**
     * 캔들 배열 얻기
     * 기존배열에서 end time 이용하여 원하는 캔들만큼 건수생성
     * @return TradeCandle candles
     */
    public TradeCandle[] getCandles(long endTime, int count) {
        TradeCandle [] candles = this.candles;
        return getCandles(candleTime, candles, endTime, count);
    }

    public static TradeCandle[] getCandles( TradeCandle [] candles, long endTime, int count){
        return getCandles(candles[0].getCloseTime()- candles[0].getOpenTime(), candles, endTime, count);
    }
    public static TradeCandle[] getCandles(long timeGap, TradeCandle [] candles, long closeTime, int count){



        int closeCandleIndex = Candles.getNearCloseTimeIndex(candles, Times.DAY_1, closeTime);
        int end = closeCandleIndex + 1;

        int startIndex = end - count;
        if(startIndex < 0){
            startIndex = 0;
        }


        TradeCandle [] newCandles = new TradeCandle[end - startIndex];
        int index = 0;

        for (int i = startIndex; i < end ; i++) {
            newCandles[index++] = candles[i];
        }

        return newCandles;
    }

    public TradeCandle getCandle(long endTime){
        TradeCandle [] candles = this.candles;

        long openTime = candles[0].getOpenTime();

        if(openTime > endTime){
            return null;
        }

        long time = endTime - openTime;

        int quotient = (int) (time/ candleTime);

        if(quotient >= candles.length){
            return null;
        }
        return candles[quotient];
    }

    /**
     * 최종 추가 시간 얻기
     * @return 최종 추가 시간
     */
    public long getLastTime() {
        return lastTime;
    }


    public TradeCandle [] getCandles(int endIndex, int count){
        return getCandles(candles, endIndex, count);
    }

    public static TradeCandle [] getCandles(TradeCandle [] candles, int endIndex, int count){
        int startIndex = endIndex - count + 1;
        if(startIndex < 0){
            startIndex = 0;
            count = endIndex - startIndex+1;
        }

//        int end = endIndex +1;
        TradeCandle [] copy = new TradeCandle[count];
//        int idx = 0;

//        for (int i = startIndex; i < end ; i++) {
//            copy[idx++] =  candles[i];
//        }
        // 속도 개선 작업으로 교체
        System.arraycopy(candles, startIndex, copy, 0, count);
        return copy;
    }


    public static BigDecimal sumVolume(TradeCandle [] candles){
        BigDecimal sum = BigDecimal.ZERO;
        for(TradeCandle candle : candles){
            sum = sum.add(candle.getVolume());
        }

        return sum;
    }






}