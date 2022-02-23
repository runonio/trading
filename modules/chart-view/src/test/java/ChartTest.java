import com.seomse.commons.utils.time.YmdUtil;
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

        MarkerData[] markerDatas = new MarkerData[12];

        for (int i = 0; i < 12; i++) {
            MarkerData markerData = new MarkerData(
                    YmdUtil.getTime("2022" + String.format("%02d",i+1) + "01")
                    ,"red",i%2 == 0 ? "Long" : "Short",i+"",
                    i%2 == 0? MarkerData.MarkerType.aboveBar : MarkerData.MarkerType.belowBar,
                    i%2 == 0? MarkerData.MarkerShape.arrowDown : MarkerData.MarkerShape.arrowUp
            );
            markerDatas[i] = markerData;
        }


        TradingChart chart = new TradingChart(candleSticks);
        chart.addMarkerAll(markerDatas);
        chart.view();
    }

    private static int randInt(int min, int max) {
        int randomNum = new Random().nextInt((max - min) + 1) + min;

        return randomNum;
    }
}