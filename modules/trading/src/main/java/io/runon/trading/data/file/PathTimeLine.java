package io.runon.trading.data.file;
/**
 * @author macle
 */
public interface PathTimeLine extends PathLastTime, TimeLine{

    PathTimeLine CSV = new CsvPathTimeLine();
    PathTimeLine JSON = new JsonPathTimeLine();

}
