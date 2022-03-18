package io.runon.trading.backtesting;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.BigDecimals;
import io.runon.trading.account.FuturesAccount;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.Strategy;
import io.runon.trading.technical.analysis.candle.CandleTime;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.candles.TradeCandles;
import io.runon.trading.view.LineData;
import io.runon.trading.view.Lines;
import io.runon.trading.view.MarkerData;
import io.runon.trading.view.TradingChart;
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
public abstract class FuturesSingleSymbolBacktesting<E extends PriceCandle> implements Runnable{

    protected E data;

    protected Strategy<E> strategy;

    protected FuturesAccount account;

    protected CandleSymbolPrice symbolPrice;

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

    private boolean isChart = false;
    private TradeCandle [] candles;
    public void setChart(TradeCandle [] candles) {
        isChart = true;
        this.candles = candles;
    }

    public void setChart(TradeCandle [] candles, int length) {
        isChart = true;
        if(candles.length > length){
            candles = TradeCandles.getCandles(candles, candles.length-1 , length);
        }
        this.candles = candles;
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
    @SuppressWarnings("ConstantConditions")
    @Override
    public void run(){
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

        List<LineData> assetList = null;
        List<MarkerData> markerDataList = null;
        List<Lines> linesList = null;

        List<LineData> lastLines = null;

        if(isChart){
            assetList = new ArrayList<>();
            markerDataList = new ArrayList<>();
            linesList = new ArrayList<>();
            lastLines = new ArrayList<>();
        }

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

                if(isChart && time >= candles[0].getOpenTime()) {
                    assetList.add(new LineData(BigDecimals.getChangePercent(startCash, account.getAssets()), time));
                    lastLines.add(new LineData(price,  time));
                }
                continue;
            }

            //차트 라인변경

            if(isChart && lastLines.size() > 0){
                if(lastPosition == Position.LONG && position != Position.LONG){
                    addLines(linesList, lastLines);
                }else if(lastPosition == Position.SHORT && position != Position.SHORT){
                    addLines(linesList, lastLines);
                } else if (
                        lastPosition != Position.LONG && lastPosition != Position.SHORT
                                && (position == Position.LONG || position == Position.SHORT)
                ) {
                    addLines(linesList, lastLines);
                }
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

            if(isChart && time >= candles[0].getOpenTime()) {
                assetList.add(new LineData(BigDecimals.getChangePercent(startCash, account.getAssets()), time));
                lastLines.add(new LineData(price, time));

                MarkerData.MarkerType markerType = MarkerData.MarkerType.aboveBar;
                MarkerData.MarkerShape markerShape = MarkerData.MarkerShape.arrowDown;

                MarkerData markerData = new MarkerData(time, "black"
                        ,lastPosition.toString() +" " + price.stripTrailingZeros().toPlainString()
                        , Long.toString(time)
                        , markerType, markerShape
                        );
                markerDataList.add(markerData);
            }

            log.info(getLogMessage());
            time = time + cycleTime;
            if(time >= endTime){
                end(assetList, markerDataList, linesList, lastLines);
                return;
            }
        }
    }

    private int chartWidth = 1200;
    private int chartHeight = 800;

    public void setChartWidth(int chartWidth) {
        this.chartWidth = chartWidth;
    }

    public void setChartHeight(int chartHeight) {
        this.chartHeight = chartHeight;
    }

    private void end(List<LineData> assetList, List<MarkerData> markerDataList, List<Lines> linesList, List<LineData> lastLines){
        log.info("backtesting end last valid time: " + CandleTime.ymdhm(lastValidTime, zoneId));
        if(isChart && candles.length > 0) {

            TradingChart chart = new TradingChart(candles, chartWidth, chartHeight, TradingChart.ChartDateType.MINUTE);
            chart.addVolume(candles);
            addLines(linesList, lastLines);
//
            if(markerDataList.size() > 0){
                chart.addMarker(markerDataList.toArray(new MarkerData[0]));
                markerDataList.clear();
            }
//
            if(assetList.size() > 0){
                chart.addLine(assetList.toArray(new LineData[0]), "#3300FF", 1, false);
                assetList.clear();
            }
//
            if(linesList.size() > 0){
                for(Lines lines : linesList){
                    chart.addLine(lines);
                }
                linesList.clear();
            }

            chart.view();

        }
    }

    private void addLines(List<Lines> linesList, List<LineData> lastLines){
        if(lastLines.size() == 0){
            return;
        }
        Lines lines = new Lines();
        lines.setLines(lastLines.toArray(new LineData[0]));
        String color;
        if(lastPosition == Position.LONG){
            color = "#CCFFCC";
        }else if(lastPosition == Position.SHORT){
            color = "#FF9999";
        }else{
            color = "#CCCCCC";
        }
        lines.setColor(color);
        lines.setSize(8);
        lines.setValueVisible(false);

        linesList.add(lines);

        lastLines.clear();
    }

    public String getLogMessage(){
        BigDecimal assets = account.getAssets();
        return  CandleTime.ymdhm(time, zoneId)+ " " + lastPosition + " " + symbolPrice.getPrice(symbol)
                + "\n" + assets.stripTrailingZeros().setScale(cashScale, RoundingMode.HALF_UP).toPlainString() + " " + BigDecimals.getChangePercent(startCash, assets) +"%";
    }
}