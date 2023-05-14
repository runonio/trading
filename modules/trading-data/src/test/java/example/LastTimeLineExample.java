package example;


import io.runon.trading.data.file.TimeFiles;

/**
 * @author macle
 */
public class LastTimeLineExample {
    public static void main(String[] args) {
        System.out.println(TimeFiles.getLastLine("D:\\data\\cryptocurrency\\spot\\candle\\BTCUSDT\\1m"));
    }
}
