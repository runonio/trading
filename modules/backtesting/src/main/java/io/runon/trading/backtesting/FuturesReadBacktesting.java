package io.runon.trading.backtesting;

import io.runon.trading.TimePrice;
import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.backtesting.price.TimePriceData;
import io.runon.trading.backtesting.price.symbol.SlippageRatePrice;

import io.runon.trading.data.TimeFileLineRead;
import io.runon.trading.order.Order;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.StrategyOrder;
import io.runon.trading.view.MarkerData;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

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
        FuturesReadBacktesting obj = this;
        TimeFileLineRead lineRead = new TimeFileLineRead() {
            @Override
            public void addLine(String line) {
                putData(make(line));
            }

            @Override
            public void end(){
                //noinspection unchecked
                obj.end(assetList, markerDataList, linesList, lastLines);
            }

        };
        lineRead.read(path);

    }

    public void putData(E timePrice){
        //noinspection unchecked
        data.setData(timePrice);
        slippageRatePrice.setPrice(symbol, timePrice);

        BigDecimal price = timePrice.getClose();
        addChartLine(price);

        Order order = strategy.getPosition(data);

        if(order.getPrice().compareTo(BigDecimal.ZERO) == 0 || order.getPosition() == Position.NONE){
            lastPosition = account.getSymbolPosition(symbol);
            return ;
        }

        account.order(symbol, order);
        lastPosition = account.getSymbolPosition(symbol);
        if(isChart){
            MarkerData.MarkerType markerType = MarkerData.MarkerType.aboveBar;
            MarkerData.MarkerShape markerShape = MarkerData.MarkerShape.arrowDown;

            MarkerData markerData = new MarkerData(time, "black"
                    ,order.getPosition().toString() +" " + order.getPrice().toPlainString() + " " + lastPosition.toString()
                    , Long.toString(time)
                    , markerType, markerShape
            );

            addChartMark(markerData);
        }


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