package io.runon.trading.data.file;

import io.runon.trading.data.csv.CsvTimeFile;

/**
 * 문자열을 활용한 lock 관리
 * @author macle
 */
public class CsvTimeLine implements TimeLine {
    @Override
    public long getTime(String line) {
        return CsvTimeFile.getTime(line);
    }

    @Override
    public String toString(){
        return "CSV";
    }

}
