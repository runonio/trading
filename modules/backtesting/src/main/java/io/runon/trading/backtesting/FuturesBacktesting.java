package io.runon.trading.backtesting;

import io.runon.trading.BigDecimals;
import io.runon.trading.backtesting.account.FuturesBacktestingAccount;
import io.runon.trading.strategy.Position;
import io.runon.trading.strategy.Strategy;
import io.runon.trading.technical.analysis.candle.CandleTime;
import io.runon.trading.technical.analysis.candle.TradeCandle;
import io.runon.trading.technical.analysis.candle.candles.TradeCandles;
import io.runon.trading.view.LineData;
import io.runon.trading.view.Lines;
import io.runon.trading.view.MarkerData;
import io.runon.trading.view.TradingChart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 선물 백테스팅 (양방향)
 * @author macle
 */
public abstract class FuturesBacktesting<E> {

    protected E data;
    protected Strategy<E> strategy;
    protected FuturesBacktestingAccount account;

    protected String symbol = "test";

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    protected ZoneId zoneId =  ZoneId.of("Asia/Seoul");
    protected int cashScale = 2;

    protected BigDecimal subtractRate = new BigDecimal("0.1");
    protected BigDecimal leverage = new BigDecimal("1");

    public void setStrategy(Strategy<E> strategy) {
        this.strategy = strategy;
    }

    public void setAccount(FuturesBacktestingAccount account) {
        this.account = account;
    }

    protected long time;

    public void setCashScale(int cashScale) {
        this.cashScale = cashScale;
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

    protected Position lastPosition = Position.NONE;

    protected BigDecimal startCash;

    public void setData(E data) {
        this.data = data;
    }

    /**
     * 계좌에 현금추가
     * @param cash 현금
     */
    public void addCash(BigDecimal cash){
        if(account == null) {
            account = new FuturesBacktestingAccount("test");
        }
        account.addCash(cash);
    }

    protected boolean isChart = false;
    protected TradeCandle[] candles;
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

    protected List<LineData> assetList = null;
    protected List<MarkerData> markerDataList = null;
    protected List<Lines> linesList = null;

    protected List<LineData> lastLines = null;


    protected int chartWidth = 1200;
    protected int chartHeight = 800;

    public void setChartWidth(int chartWidth) {
        this.chartWidth = chartWidth;
    }

    public void setChartHeight(int chartHeight) {
        this.chartHeight = chartHeight;
    }

    protected void end(List<LineData> assetList, List<MarkerData> markerDataList, List<Lines> linesList, List<LineData> lastLines){

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

    protected void addLines(List<Lines> linesList, List<LineData> lastLines){
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

    protected void init(){

        if(account == null) {
            account = new FuturesBacktestingAccount("test");
            account.addCash(new BigDecimal(10000));
        }else if(account.getCash().compareTo(BigDecimal.ZERO) ==0){
            //설정하지 않았으면 10000달러로 설정
            account.addCash(new BigDecimal(10000));
        }

        startCash = account.getCash();

        if(isChart){
            assetList = new ArrayList<>();
            markerDataList = new ArrayList<>();
            linesList = new ArrayList<>();
            lastLines = new ArrayList<>();
        }
    }

    protected void addChartLine(BigDecimal price){
        if(isChart && time >= candles[0].getOpenTime()) {
            assetList.add(new LineData(BigDecimals.getChangePercent(startCash, account.getAssets()), time));
            lastLines.add(new LineData(price,  time));
        }
    }


    public void changeChartLine(Position position){
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
    }

    public void addChartMark(BigDecimal price){
        if(isChart && time >= candles[0].getOpenTime()) {
            MarkerData.MarkerType markerType = MarkerData.MarkerType.aboveBar;
            MarkerData.MarkerShape markerShape = MarkerData.MarkerShape.arrowDown;

            MarkerData markerData = new MarkerData(time, "black"
                    ,lastPosition.toString() +" " + price.stripTrailingZeros().toPlainString()
                    , Long.toString(time)
                    , markerType, markerShape
            );
            markerDataList.add(markerData);
        }
    }


    protected void trade(BigDecimal price, Position position){

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
    }


    public FuturesBacktestingAccount getAccount(){
        return account;
    }


    public String getLogMessage(BigDecimal price){
        BigDecimal assets = account.getAssets();
        return  CandleTime.ymdhm(time, zoneId)+ " " + lastPosition + " " + price
                + "\n" + assets.stripTrailingZeros().setScale(cashScale, RoundingMode.HALF_UP).toPlainString() + " " + BigDecimals.getChangePercent(startCash, assets) +"%";
    }
}
