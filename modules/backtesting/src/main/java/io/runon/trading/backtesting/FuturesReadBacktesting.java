package io.runon.trading.backtesting;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.CandleTimes;
import io.runon.trading.TimePrice;
import io.runon.trading.account.FuturesPosition;
import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.backtesting.price.TimePriceData;
import io.runon.trading.backtesting.price.symbol.SlippageRatePrice;
import io.runon.trading.data.file.TimeFileLineRead;
import io.runon.trading.order.MarketOrderCash;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.StrategyOrder;
import io.runon.trading.view.MarkerData;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 실시간정보를 (초단위) 백테스팅하기위해 만든도구
 * 분할 매수 분할매도 방식
 * @author macle
 */
@SuppressWarnings("rawtypes")
@Slf4j
public abstract class FuturesReadBacktesting<E extends TimePrice, T extends TimePriceData> extends FuturesBacktesting<T> {

    protected final SlippageRatePrice slippageRatePrice = new SlippageRatePrice();

    protected StrategyOrder<T> strategy;



    public FuturesReadBacktesting(){
        account = new FuturesBacktestingAccount("test");
        account.addCash(new BigDecimal(10000));
        account.setSymbolPrice(slippageRatePrice);
    }

    public void setStrategy(StrategyOrder<T> strategy) {
        this.strategy = strategy;
    }

    public void start(String path){
        init();
        FuturesReadBacktesting obj = this;
        TimeFileLineRead lineRead = new TimeFileLineRead() {
            @Override
            public void addLine(String line) {
                putData(make(line));
            }

            @Override
            public void end(){
                obj.end();
            }

        };
        lineRead.read(path);
    }

    private long candleOpenTime = 0;

    public void putData(E timePrice){

        time = timePrice.getTime();

        long openTime = CandleTimes.getOpenTime(Times.MINUTE_1, time);

        //noinspection unchecked
        data.setData(timePrice);

        slippageRatePrice.setPrice(symbol, timePrice);

        BigDecimal price = timePrice.getClose();
        if(openTime != candleOpenTime) {
            addChartLine(price);
            candleOpenTime = openTime;
        }

        MarketOrderCash order = strategy.getPosition(data);
        if(order.getCash().compareTo(BigDecimal.ZERO) == 0 || order.getPosition() == Position.NONE){
            lastPosition = account.getSymbolPosition(symbol);
            return ;
        }

        account.order(symbol, order);

        lastPosition = account.getSymbolPosition(symbol);
        if(isChart){
            MarkerData.MarkerType markerType = MarkerData.MarkerType.aboveBar;
            MarkerData.MarkerShape markerShape = MarkerData.MarkerShape.arrowDown;

            String color;
            if(order.getPosition() == Position.LONG){
                color = "#003300";
            }else{
                color = "#990000";
            }

            MarkerData markerData = new MarkerData(time, color
                    ,order.getPosition().toString() + " " + order.getCash() +" "+ account.getAssets().setScale(2, RoundingMode.DOWN).stripTrailingZeros().toPlainString()
                    , Long.toString(openTime)
                    , markerType, markerShape
            );

            addChartMark(markerData);
        }



        FuturesPosition futuresPosition  = account.getPosition(symbol);

        log.info(getLogMessage(price));
    }

    public abstract E make(String line);

    public void setSlippageRate(BigDecimal slippageRate){
        slippageRatePrice.setRate(slippageRate);
    }

    /**
     * 구매 수수료 설정
     * @param buyFee 구매수수료
     */
    public void setBuyFee(BigDecimal buyFee) {
        account.setBuyFee(buyFee);
    }

    /**
     * 판매수수료 설정
     * @param sellFee 판매수수료
     */
    public void setSellFee(BigDecimal sellFee) {
        account.setSellFee(sellFee);
    }

}