package io.runon.trading.backtesting;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.BigDecimals;
import io.runon.trading.strategy.Position;
import io.runon.trading.technical.analysis.candle.CandleTime;
import io.runon.trading.view.LineData;
import io.runon.trading.view.Lines;
import io.runon.trading.view.MarkerData;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.List;

/**
 * 선물 단일종목 벡테스팅
 * @author macle
 */
@Slf4j
public abstract class FuturesSingleSymbolBacktesting<E extends PriceCandle> extends FuturesBacktesting<E> implements Runnable{

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
                log.info(getLogMessage());
                end(assetList, markerDataList, linesList, lastLines);
                return;
            }

            symbolPrice.setCandle(symbol, data.getPriceCandle());

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

            //차트 라인변경
            changeChartLine(position);

            if(position == Position.LONG){
                //숏매도 롱매수
                account.sell(symbol, Position.SHORT);
                account.buy(account.getBuyQuantity(subtractRate, symbol, Position.LONG, leverage));
                lastPosition = Position.LONG;
            }else if(position == Position.SHORT){
                //롱매도 숏매수
                account.sell(symbol, Position.LONG);
                account.buy(account.getBuyQuantity(subtractRate, symbol, Position.SHORT, leverage));
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

            addChartLine(price);
            addChartMark(price);

            log.info(getLogMessage());
            time = time + cycleTime;
            if(time >= endTime){
                end(assetList, markerDataList, linesList, lastLines);
                return;
            }
        }
    }

    @Override
    protected void end(List<LineData> assetList, List<MarkerData> markerDataList, List<Lines> linesList, List<LineData> lastLines){
        log.info("backtesting end last valid time: " + CandleTime.ymdhm(lastValidTime, zoneId));
        super.end(assetList, markerDataList, linesList, lastLines);
    }


    public String getLogMessage(){
        BigDecimal assets = account.getAssets();
        return  CandleTime.ymdhm(time, zoneId)+ " " + lastPosition + " " + symbolPrice.getPrice(symbol)
                + "\n" + assets.stripTrailingZeros().setScale(cashScale, RoundingMode.HALF_UP).toPlainString() + " " + BigDecimals.getChangePercent(startCash, assets) +"%";
    }
}