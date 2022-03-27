package io.runon.trading.backtesting;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.backtesting.price.PriceCandle;
import io.runon.trading.backtesting.price.symbol.CandleSymbolPrice;
import io.runon.trading.backtesting.price.symbol.SlippageRandomSymbolPrice;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.Strategy;
import io.runon.trading.technical.analysis.candle.CandleTime;
import io.runon.trading.view.LineData;
import io.runon.trading.view.Lines;
import io.runon.trading.view.MarkerData;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;

/**
 * 선물 단일종목 벡테스팅
 * 한번에 전부 포지션 전환한다는 가정의전략
 *
 * @author macle
 */
@Slf4j
public abstract class FuturesSingleSymbolBacktesting<E extends PriceCandle> extends FuturesBacktesting<E> implements Runnable{

    protected Strategy<E> strategy;
    public void setStrategy(Strategy<E> strategy) {
        this.strategy = strategy;
    }

    protected CandleSymbolPrice symbolPrice;

    //1분에 한번 판단
    protected long cycleTime = Times.MINUTE_1;
    protected ZoneId zoneId =  ZoneId.of("Asia/Seoul");

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

    public void setSymbolPrice(CandleSymbolPrice symbolPrice) {
        this.symbolPrice = symbolPrice;
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

        if(symbolPrice == null){
            symbolPrice = new SlippageRandomSymbolPrice();
        }

        account.setSymbolPrice(symbolPrice);
        time = startTime;

        for(;;) {

            changeTime(time);

            if(isEnd){
                //구현체에서 종료이벤트가 발생하였으면
                log.info(getLogMessage(symbolPrice.getPrice(symbol)));
                end(assetList, markerDataList, linesList, lastLines);
                return;
            }

            symbolPrice.setPrice(symbol, data.getPriceCandle());

            if(!data.isValid(time)){
                time = time + cycleTime;
                if(time >= endTime){
                    end(assetList, markerDataList, linesList, lastLines);
                    return;
                }
                continue;
            }

            lastValidTime = time;
            BigDecimal price = symbolPrice.getPrice(symbol);
            Position position = strategy.getPosition(data);
            if(lastPosition == position || position == Position.NONE){
                time = time + cycleTime;

                if(time >= endTime){
                    end(assetList, markerDataList, linesList, lastLines);
                    return;
                }
                addChartLine(price);
                continue;
            }

            changeChartLine(position);
            if(position == Position.LONG){
                //숏매도 롱매수
                account.buyAll(symbol);
            }else if(position == Position.SHORT){
                //롱매도 숏매수
                account.sellAll(symbol);
            }else if(position == Position.LONG_CLOSE){
                //롱매도
                account.longClose(symbol);
            }else if(position == Position.SHORT_CLOSE){
                //숏매도
                account.shortClose(symbol);
            }else if(position == Position.CLOSE){
                //롱 숏 둘다매도
                account.close(symbol);
            }

            lastPosition = position;

            addChartLine(price);
            addChartMark(price);

            log.info(getLogMessage(price));
            time = time + cycleTime;
            if(time >= endTime){
                end(assetList, markerDataList, linesList, lastLines);
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
    protected void end(List<LineData> assetList, List<MarkerData> markerDataList, List<Lines> linesList, List<LineData> lastLines){
        log.info("backtesting end last valid time: " + CandleTime.ymdhm(lastValidTime, zoneId));
        super.end(assetList, markerDataList, linesList, lastLines);
    }

}