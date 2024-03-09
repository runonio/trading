package io.runon.trading.data.storage;

import io.runon.trading.data.DataPathTimeRange;
import io.runon.trading.data.file.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author macle
 */
public class StorageTimeRange {

    public static DataPathTimeRange[]  getTimeRanges(String dataDirPath) {
        File dataDir = new File(dataDirPath);
        return getTimeRanges(dataDir);
    }

    public static DataPathTimeRange[] getTimeRanges(File dataDir){

        List<DataPathTimeRange> list = new ArrayList<>();
        File [] lastDirFiles = Files.getLastChildDirs(dataDir);

        for(File lastDirFile : lastDirFiles){

            DataPathTimeRange dataPathTimeRange =  Files.getTimeRange(lastDirFile);
            if(dataPathTimeRange == null){
                continue;
            }

            list.add(dataPathTimeRange);
        }

        return list.toArray(new DataPathTimeRange[0]);
    }

    public static void consoleViewTimeRange(String path){
        consoleViewTimeRange(new File(path));
    }
    public static void consoleViewTimeRange(File dataDir){
        File [] lastDirFiles = Files.getLastChildDirs(dataDir);
        for(File lastDirFile : lastDirFiles){

            DataPathTimeRange dataPathTimeRange =  Files.getTimeRange(lastDirFile);
            if(dataPathTimeRange == null){
                continue;
            }
            System.out.println(dataPathTimeRange);
        }
    }

    public static void main(String[] args) {
//        DataPathTimeRange [] dataPathTimeRanges = getTimeRanges(new File("D:\\data\\stock"));

        consoleViewTimeRange("D:\\data\\stock");

//        for(DataPathTimeRange dataPathTimeRange : dataPathTimeRanges){
//            System.out.println(dataPathTimeRange);
//        }
    }
}
