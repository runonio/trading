package io.runon.trading.data;

import io.runon.trading.TradingConfig;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * @author macle
 */
public class TradingDataPath {

    public static String getAbsolutePath(String dirPath){

        dirPath = changeSeparator(dirPath);

        String dataPth = TradingConfig.getTradingDataPath();

        if(dirPath.startsWith(dataPth)){
            return dirPath;
        }else{
            String fileSeparator = FileSystems.getDefault().getSeparator();
            return  dataPth + fileSeparator + dirPath;
        }
    }

    public static String changeSeparator(String path){
        String fileSeparator = FileSystems.getDefault().getSeparator();

        if(fileSeparator.equals("\\")){
            return path.replace("/", "\\");
        }else{
            return path.replace("\\", "/");
        }
    }


    public static String getRelativePath(String path){
        path = changeSeparator(path);
        String dataPath = TradingConfig.getTradingDataPath();

        if(path.startsWith(dataPath)){
            path = path.substring(dataPath.length()+1);
        }

        return path;
    }

    public static String getDataPath(){
        return TradingConfig.getTradingDataPath();
    }

    public static String getFuturesCandlePath(String dataClassification, String itemId){
        String fileSeparator = FileSystems.getDefault().getSeparator();
        String dataPath = TradingConfig.getTradingDataPath();
        return dataPath + fileSeparator + dataClassification + fileSeparator + "futures" + fileSeparator + "candle" + fileSeparator + itemId ;
    }

    public static String getFuturesCandleRelativePathPath(String dataClassification, String itemId){
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return  dataClassification + fileSeparator + "futures" + fileSeparator + "candle" + fileSeparator + itemId ;
    }


    public static String getFuturesCandlePath(String dataClassification, String itemId, String interval){
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return getFuturesCandlePath(dataClassification, itemId) + fileSeparator + interval;
    }


    public static String [] getDirNames(String path){
        path = getAbsolutePath(path);

        List<String> list = new ArrayList<>();

        File file = new File(path);
        if(!file.isDirectory()){
            return new String[0];
        }

        File [] files = file.listFiles();
        if(files == null){
            return new String[0];
        }

        for(File f : files){
            if(f.isDirectory()){
                list.add(f.getName());
            }
        }

        return list.toArray(new String[0]);
    }


    public static void main(String[] args) {

        String [] dirNames = getDirNames("indices\\futures\\candle");
        for(String dirName: dirNames){
            System.out.println(dirName);
        }
    }


}
