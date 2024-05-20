package io.runon.trading.data.file;

import io.runon.trading.data.csv.CsvTimeFile;

/**
 * 문자열을 활용한 lock 관리
 * @author macle
 */
public class CsvPathTimeLine implements PathTimeLine{
    @Override
    public long getLastTime(String path) {
        return CsvTimeFile.getLastTime(path);
    }

    @Override
    public long getTime(String line) {
        return CsvTimeFile.getTime(line);
    }
}
