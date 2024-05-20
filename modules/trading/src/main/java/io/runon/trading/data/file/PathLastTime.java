package io.runon.trading.data.file;
/**
 * @author macle
 */
public interface PathLastTime {

    PathLastTime CSV = new CsvPathLastTime();
    PathLastTime JSON = new JsonPathLastTime();

    long getLastTime(String path);
}
