package io.runon.trading.data.file;

import io.runon.trading.data.csv.CsvTimeFile;

/**
 * @author macle
 */
public class CsvPathLastTime implements PathLastTime{
    @Override
    public long getLastTime(String path) {
        return CsvTimeFile.getLastTime(path);
    }

    @Override
    public String toString(){
        return "CSV";
    }
}
