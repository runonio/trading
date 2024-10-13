package io.runon.trading.data;

import io.runon.trading.TradingConfig;

import java.nio.file.FileSystems;

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
        String dataPath = TradingConfig.getTradingDataPath();
        return  dataClassification + fileSeparator + "futures" + fileSeparator + "candle" + fileSeparator + itemId ;
    }


    public static String getFuturesCandlePath(String dataClassification, String itemId, String interval){
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return getFuturesCandlePath(dataClassification, itemId) + fileSeparator + interval;
    }


    public static void main(String[] args) {
        String path = getFuturesCandleRelativePathPath("indices","USA_snp_500_vix");
        System.out.println(path);
    }


}
