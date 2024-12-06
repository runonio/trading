package io.runon.trading.backtesting;

import io.runon.commons.utils.time.Times;
import io.runon.trading.TradingConfig;
import io.runon.trading.backtesting.price.PriceCandle;
import io.runon.trading.backtesting.price.CandlePrice;
import io.runon.trading.backtesting.price.SlippageRandomPrice;
import io.runon.trading.backtesting.strategy.OrderStrategy;
import io.runon.trading.backtesting.strategy.PositionStrategy;
import io.runon.trading.backtesting.strategy.TradingStrategy;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.Strategy;
import io.runon.trading.strategy.StrategyOrderCash;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 선물 단일종목 벡테스팅
 * 한번에 전부 포지션 전환한다는 가정의전략
 *
 * @author macle
 */
@Slf4j
public abstract class FuturesSingleBacktesting<E extends PriceCandle> extends FuturesBacktesting<E> implements Runnable{

    boolean isLogging = true;

    public void setLogging(boolean logging) {
        isLogging = logging;
    }

    protected TradingStrategy<E> strategy;
    public void setStrategy(Object strategy) {
        if(strategy instanceof Strategy){
            //noinspection unchecked
            this.strategy = new PositionStrategy<>((Strategy<E>)strategy);
        }else if(strategy instanceof StrategyOrderCash<?>){
            //noinspection unchecked
            this.strategy = new OrderStrategy<>((StrategyOrderCash<E>) strategy);
        }else {
            throw new IllegalArgumentException("class not support: " + strategy.getClass().getName());
        }

    }

    protected CandlePrice idPrice;

    //1분에 한번 판단
    protected long cycleTime = Times.MINUTE_1;

    protected final long beginTime;
    protected final long endTime;

    public FuturesSingleBacktesting(long beginTime, long endTime){
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public FuturesSingleBacktesting(long startTime){
        this.beginTime = startTime;
        this.endTime = System.currentTimeMillis();
    }

    public void setIdPrice(CandlePrice idPrice) {
        this.idPrice = idPrice;
    }

    public void setCycleTime(long cycleTime) {
        this.cycleTime = cycleTime;
    }

    //구현체에서의 종료이벤트 발생
    protected boolean isEnd = false;

    protected long lastValidTime;

    //백테스팅 실행
    //log에 매수매두 시점을 보여줌 // 이후에는 각 차트로 표시
    @Override
    public void run(){

        super.init();

        if(idPrice == null){
            idPrice = new SlippageRandomPrice();
        }

        account.setIdPrice(idPrice);
        time = beginTime;

        for(;;) {

            changeTime(time);

            if(isEnd){
                //구현체에서 종료이벤트가 발생하였으면
                if(isLogging) {
                    log.info(getLogMessage(idPrice.getPrice(symbol)));
                }
                end();
                return;
            }

            idPrice.setPrice(symbol, data.getPriceCandle());

            if(!data.isValid(time)){
                time = time + cycleTime;
                if(time >= endTime){
                    end();
                    return;
                }
                continue;
            }

            lastValidTime = time;

            BigDecimal price = idPrice.getPrice(symbol);
            Position position = strategy.getPosition(data);
            if(lastPosition == position || position == Position.NONE){
                time = time + cycleTime;

                if(time >= endTime){
                    end();
                    return;
                }
                addChartLine(price);
                continue;
            }

            changeChartLine(position);

            strategy.trade(account, symbol);

            lastPosition = position;

            addChartLine(price);
            addChartMark(price);
            if(isLogging) {
                log.info(getLogMessage(price));
            }
            time = time + cycleTime;
            if(time >= endTime){
                end();
                return;
            }
        }
    }

    /**
     * 시간변화에 따라 변해야 하는 데이터구조등의 내용 변경
     * @param time 기준시간
     */
    public abstract void changeTime(long time);

    @Override
    protected void end(){
        if(isLogging) {
            log.info("backtesting end last valid time: " + Times.ymdhm(lastValidTime, TradingConfig.DEFAULT_TIME_ZONE_ID));
        }
        super.end();
    }

}