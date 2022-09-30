package io.runon.trading.data.csv;

import io.runon.trading.data.file.TimeFilePathChange;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * csv 형식
 * @author macle
 */
public class CsvTimeFilePathChange extends TimeFilePathChange {


    @Override
    public long getTime(String line) {
        return CsvTimeFile.getTime(line);
    }
}
