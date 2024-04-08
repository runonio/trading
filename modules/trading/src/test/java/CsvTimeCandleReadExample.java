import com.seomse.commons.utils.time.Times;
import io.runon.trading.data.csv.CsvCandle;
import io.runon.trading.data.csv.CsvTimeCandleRead;
import io.runon.trading.technical.analysis.candle.TimeCandle;


/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class CsvTimeCandleReadExample extends CsvTimeCandleRead {

    public CsvTimeCandleReadExample(long time) {
        super(time);
    }

    @Override
    public void addTimeCandle(TimeCandle timeCandle) {
        System.out.println(timeCandle.getTime() + " " + CsvCandle.value(timeCandle.getCandle()));
    }

    public static void main(String[] args) {
        CsvTimeCandleReadExample example = new CsvTimeCandleReadExample(Times.MINUTE_1);
        example.read("C:\\data\\trd\\cr");
    }

}
