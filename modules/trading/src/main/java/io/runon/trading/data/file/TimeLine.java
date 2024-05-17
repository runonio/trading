package io.runon.trading.data.file;
/**
 * 시계열 라인데이터 구조
 * @author macle
 */
public interface TimeLine {

    TimeLine CSV = new CsvTimeLine();
    TimeLine JSON = new JsonTimeLine();

    long getTime(String line);

}
