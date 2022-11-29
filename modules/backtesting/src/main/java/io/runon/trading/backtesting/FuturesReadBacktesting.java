package io.runon.trading.backtesting;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.CandleTimes;
import io.runon.trading.TimePrice;
import io.runon.trading.account.FuturesPosition;
import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.backtesting.price.TimePriceData;
import io.runon.trading.backtesting.price.symbol.SlippageRatePrice;
import io.runon.trading.data.file.TimeFileLineRead;
import io.runon.trading.data.file.TimeName;
import io.runon.trading.order.MarketOrderCash;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.StrategyOrder;
import io.runon.trading.view.MarkerData;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;

/**
 * 실시간정보를 (초단위) 백테스팅하기위해 만든도구
 * 분할 매수 분할매도 방식
 * @author macle
 */
@SuppressWarnings("rawtypes")
@Slf4j
public abstract class FuturesReadBacktesting<E extends TimePrice, T extends TimePriceData> extends FuturesBacktesting<T> implements Runnable{

    protected final SlippageRatePrice slippageRatePrice = new SlippageRatePrice();

    protected StrategyOrder<T> strategy;

    protected final String path;

    public FuturesReadBacktesting(String path){
        this.path = path;

        account = new FuturesBacktestingAccount("test");
        account.addCash(new BigDecimal(10000));
        account.setSymbolPrice(slippageRatePrice);
    }

    public void setStrategy(StrategyOrder<T> strategy) {
        this.strategy = strategy;
    }

    protected long startTime = -1;
    protected long endTime = -1;

    protected long accumulateTime = -1;

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setAccumulateTime(long accumulateTime) {
        this.accumulateTime = accumulateTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    protected TimeName.Type fileTimeType = TimeName.Type.DAY_5;
    protected ZoneId zoneId = CandleTimes.UTC_ZONE_ID;

    public void setFileTimeType(TimeName.Type fileTimeType) {
        this.fileTimeType = fileTimeType;
    }

    @Override
    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public void run(){
        init();
        FuturesReadBacktesting obj = this;
        TimeFileLineRead lineRead = new TimeFileLineRead() {
            @Override
            public void addLine(String line) {

                E data = make(line);

                if(startTime >= 0 && data.getTime() < startTime){
                    if(accumulateTime >= 0 && data.getTime() >= accumulateTime){
                        accumulateData(data);
                    }
                    return;
                }

                if(endTime >= 0 && data.getTime() >= endTime){
                    stop();
                    return;
                }

                putData(data);
            }

            @Override
            public void end(){
                obj.end();
            }

        };

        if(startTime >= 0){
            lineRead.setStartName(TimeName.getName(startTime, fileTimeType, zoneId));
        }

        if(endTime >= 0){
            lineRead.setEndName(TimeName.getName(endTime, fileTimeType, zoneId));
        }



        lineRead.read(path);
    }

    /**
     * 데이터 축척.
     * 실제 환경에서 축척된 데이터가 필요해서 데이터 축척이 필요할 경우에만 오버라이딩 해서 사용용
     *@param data 축척용 데이터
     */
    public void accumulateData(E data){

    }


    private long candleOpenTime = 0;

    public void putData(E timeData){

        time = timeData.getTime();

        long openTime = CandleTimes.getOpenTime(Times.MINUTE_1, time);

        //noinspection unchecked
        data.setData(timeData);

        slippageRatePrice.setPrice(symbol, timeData);

        BigDecimal price = timeData.getClose();
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