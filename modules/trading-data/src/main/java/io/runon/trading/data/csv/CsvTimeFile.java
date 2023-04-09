package io.runon.trading.data.csv;

import io.runon.trading.data.file.TimeFiles;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * @author macle
 */
public class CsvTimeFile {

    public static long getTime(String csvLine){
        int index = csvLine.indexOf(',');
        return Long.parseLong(csvLine.substring(0,index));
    }

    public static long getLastOpenTime(String path){

        String lastLine = TimeFiles.getLastLine(path);

        if(lastLine == null){
            return -1;
        }
        int index = lastLine.indexOf(',');
        return Long.parseLong(lastLine.substring(0, index));
    }
}
