import io.runon.trading.data.candle.CandleDataUtils;

/**
 * @author macle
 */
public class CandlePathsExample {
    public static void main(String[] args) {
        String [] paths = CandleDataUtils.getCandlePaths("D:\\data");
        for(String path: paths){
            System.out.println(path);
        }

        System.out.println(paths.length);
    }
}
