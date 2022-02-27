package io.runon.trading.backtesting;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.BigDecimals;
import io.runon.trading.account.FuturesAccount;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.Strategy;
import io.runon.trading.technical.analysis.candle.CandleTime;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 선물 단일종목 벡테스팅
 * @author macle
 */
@Slf4j
public abstract class FuturesSingleSymbolBacktesting<E extends PriceCandle> {

    protected E data;

    protected Strategy<E> strategy;

    protected FuturesAccount account;

    protected CandleSymbolPrice symbolPrice;

    protected List<TradeHistory> tradeHistoryList = new ArrayList<>();

    //1분에 한번 판단
    protected long cycleTime = Times.MINUTE_1;
    protected ZoneId zoneId =  ZoneId.of("Asia/Seoul");
    protected int cashScale = 2;

    protected BigDecimal subtractRate = new BigDecimal("0.1");
    protected BigDecimal leverage = new BigDecimal("1");

    protected final long startTime;
    protected final long endTime;

    public FuturesSingleSymbolBacktesting(long startTime, long endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public FuturesSingleSymbolBacktesting(long startTime){
        this.startTime = startTime;
        this.endTime = System.currentTimeMillis();
    }

    public void setStrategy(Strategy<E> strategy) {
        this.strategy = strategy;
    }

    public void setAccount(FuturesAccount account) {
        this.account = account;
    }

    public void setSymbolPrice(CandleSymbolPrice symbolPrice) {
        this.symbolPrice = symbolPrice;
    }

    public void setCycleTime(long cycleTime) {
        this.cycleTime = cycleTime;
    }

    public void setCashScale(int cashScale) {
        this.cashScale = cashScale;
    }

    public void setSubtractRate(BigDecimal subtractRate) {
        this.subtractRate = subtractRate;
    }

    public BigDecimal getLeverage() {
        return leverage;
    }

    public void setLeverage(BigDecimal leverage) {
        this.leverage = leverage;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * 계좌에 현금추가
     * @param cash 현금
     */
    public void addCash(BigDecimal cash){
        if(account == null) {
            account = new FuturesAccount("test");
        }
        account.addCash(cash);
    }

    protected String symbol = "test";

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    //구현체에서의 종료이벤트 발생
    protected boolean isEnd = false;

    /**
     * 시간변화에 따라 변해야 하는 데이터구조등의 내용 변경
     * @param time 기준시간
     */
    public abstract void changeTime(long time);

    protected BigDecimal startCash;

    protected Position lastPosition = Position.NONE;
    protected long time;
    protected long lastValidTime;

    //백테스팅 실행
    //log에 매수매두 시점을 보여줌 // 이후에는 각 차트로 표시

    public void runLog(){
        if(account == null) {
            account = new FuturesAccount("test");
            account.addCash(new BigDecimal(10000));
        }else if(account.getCash().compareTo(BigDecimal.ZERO) ==0){
            //설정하지 않았으면 10000달러로 설정
            account.addCash(new BigDecimal(10000));
        }

        if(symbolPrice == null){
            symbolPrice = new SlippageRandomSymbolPrice();
        }

        account.setSymbolPrice(symbolPrice);
        startCash = account.getCash();
        time = startTime;
        for(;;) {

            changeTime(time);

            if(isEnd){
                //구현체에서 종료이벤트가 발생하였으면
                log.info(getLogMessage());
                log.info("backtesting end(event) last valid time: " + CandleTime.ymdhm(lastValidTime, zoneId));
                return;
            }

            symbolPrice.setCandle(symbol, data.getPriceCandle());

            if(!data.isValid(time)){
                time = time + cycleTime;
                continue;
            }

            lastValidTime = time;

            Position position = strategy.getPosition(data);

            if(lastPosition == position || position == Position.NONE){
                time = time + cycleTime;
                continue;
            }

            if(position == Position.LONG){
                //숏매도 롱매수
                account.sell(symbol, Position.SHORT);
                account.buy(account.getBuyAmount(subtractRate, symbol, Position.LONG, leverage));
                lastPosition = Position.LONG;
            }else if(position == Position.SHORT){
                //롱매도 숏매수
                account.sell(symbol, Position.LONG);
                account.buy(account.getBuyAmount(subtractRate, symbol, Position.SHORT, leverage));
                lastPosition = Position.SHORT;
            }else if(position == Position.LONG_CLOSE){
                //롱매도
                account.sell(symbol, Position.LONG);
                lastPosition = Position.LONG_CLOSE;
            }else if(position == Position.SHORT_CLOSE){
                //숏매도
                account.sell(symbol, Position.SHORT);
                lastPosition = Position.SHORT_CLOSE;
            }else if(position == Position.CLOSE){
                //롱 숏 둘다매도
                account.sell(symbol, Position.LONG);
                account.sell(symbol, Position.SHORT);
                lastPosition = Position.CLOSE;
            }

            // 트레이드 이력 저장
            tradeHistoryList.add(new TradeHistory(time, lastPosition));

            log.info(getLogMessage());
            time = time + cycleTime;
            if(time >= endTime){
                log.info("backtesting end last valid time: " + CandleTime.ymdhm(lastValidTime, zoneId));
                return;
            }
        }
    }

    public String getLogMessage(){
        BigDecimal assets = account.getAssets();
        return  CandleTime.ymdhm(time, zoneId)+ " " + lastPosition + " " + symbolPrice.getPrice(symbol)
                + "\n" + assets.stripTrailingZeros().setScale(cashScale, RoundingMode.HALF_UP).toPlainString() + " " + BigDecimals.getChangePercent(startCash, assets) +"%";
    }

    public List<TradeHistory> getTradeHistoryList(){
        return this.tradeHistoryList;
    }

}
