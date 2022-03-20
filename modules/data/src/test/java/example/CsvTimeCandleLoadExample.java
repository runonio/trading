package example;

import com.seomse.commons.utils.time.Times;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.csv.CsvTimeCandleLoad;
import io.runon.trading.technical.analysis.candle.TimeCandle;


/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class CsvTimeCandleLoadExample extends CsvTimeCandleLoad {

    @Override
    public void addTimeCandle(TimeCandle timeCandle) {
        System.out.println(timeCandle.getTime() + " " + CsvCandle.value(timeCandle.getCandle()));
    }

    public static void main(String[] args) {
        CsvTimeCandleLoadExample example = new CsvTimeCandleLoadExample();
        example.load("data/cr", Times.MINUTE_1);
    }

}
