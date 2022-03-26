package io.runon.trading.backtesting;

import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.TimePrice;
import io.runon.trading.backtesting.price.TimePriceData;
import io.runon.trading.backtesting.price.symbol.SlippageRatePrice;
import io.runon.trading.data.TimeFileLineRead;
import io.runon.trading.strategy.Position;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 초단위 매매 백테스팅
 * @author macle
 */
@SuppressWarnings("rawtypes")
@Slf4j
public abstract class FuturesSecondBacktesting<E extends TimePrice, T extends TimePriceData> extends FuturesBacktesting<T> {

    private final SlippageRatePrice slippageRatePrice = new SlippageRatePrice();


    public FuturesSecondBacktesting(){
        account = new FuturesBacktestingAccount("test");
        account.addCash(new BigDecimal(10000));
        account.setSymbolPrice(slippageRatePrice);
    }

    public void start(String path){

        TimeFileLineRead lineRead = new TimeFileLineRead() {
            @Override
            public void addLine(String line) {
                putData(make(line));
            }
        };
        lineRead.read(path);
    }

    public void putData(E timePrice){
        //noinspection unchecked
        data.setData(timePrice);
        slippageRatePrice.setPrice(symbol, timePrice);

        BigDecimal price = timePrice.getClose();

        Position position = strategy.getPosition(data);
        if(lastPosition == position || position == Position.NONE){
            addChartLine(price);
            return;
        }

        trade(price, position);

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