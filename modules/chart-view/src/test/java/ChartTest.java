import io.runon.commons.utils.time.YmdUtil;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.view.MarkerData;
import io.runon.trading.view.TradingChart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class ChartTest {
    public static void main(String[] args) {
        List<String> ymdList = YmdUtil.getYmdList("20220101", "20221231");

        CandleStick[] candleSticks = new CandleStick[ymdList.size()];
        TimeNumber[] lines = new TimeNumber[ymdList.size()];
        TimeNumber[] lines2 = new TimeNumber[ymdList.size()];
        for (int i = 0; i < ymdList.size(); i++) {
            String ymd = ymdList.get(i);

            CandleStick candleStick = new CandleStick();

            int randomNum = randInt(100, 200);
            candleStick.setOpen(new BigDecimal(randomNum));
            randomNum = randInt(100, 200);
            candleStick.setClose(new BigDecimal(randomNum));
            randomNum = randInt(100, 200);
            candleStick.setHigh(new BigDecimal(randomNum));
            randomNum = randInt(100, 200);
            candleStick.setLow(new BigDecimal(randomNum));
            candleStick.setOpenTime(YmdUtil.getTime(ymd));
            candleSticks[i] = candleStick;
        }


        for (int i = 0; i < ymdList.size(); i++) {
            String ymd = ymdList.get(i);

            int randomNum = randInt(0, 10);
            TimeNumber lineData = new TimeNumberData(YmdUtil.getTime(ymd), new BigDecimal(randomNum));
            lines[i] = lineData;
        }

        for (int i = 0; i < ymdList.size(); i++) {
            String ymd = ymdList.get(i);

            int randomNum = randInt(100,200);
            TimeNumber lineData = new TimeNumberData(YmdUtil.getTime(ymd), new BigDecimal(randomNum));
            lines2[i] = lineData;
        }


        MarkerData[] markerDataArr = new MarkerData[12];

        for (int i = 0; i < 12; i++) {
            MarkerData markerData = new MarkerData(
                    YmdUtil.getTime("2022" + String.format("%02d",i+1) + "01")
                    ,"red",i%2 == 0 ? "Long" : "Short",i+"",
                    i%2 == 0? MarkerData.MarkerType.aboveBar : MarkerData.MarkerType.belowBar,
                    i%2 == 0? MarkerData.MarkerShape.arrowDown : MarkerData.MarkerShape.arrowUp
            );
            markerDataArr[i] = markerData;
        }


        TradingChart chart = new TradingChart(candleSticks);
        chart.addMarker(markerDataArr);
        chart.addLine(lines, "blue", 2, false);
        chart.addLine(lines2, "blue", 1);


        TradingChart chart2 = new TradingChart(candleSticks);
        chart2.addMarker(markerDataArr);
        chart.addAnotherChart(chart2);

        TradingChart chart3 = new TradingChart(candleSticks);
        chart.addAnotherChart(chart3);

        chart.view();
    }

    private static int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
