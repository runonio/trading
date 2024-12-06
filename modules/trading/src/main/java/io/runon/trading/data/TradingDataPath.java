package io.runon.trading.data;

import io.runon.commons.config.Config;
import io.runon.trading.CountryUtils;
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

    public static String getTempPath(){
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return getDataPath()+fileSeparator + "temp";
    }

    public static String getTempPath(String relativePath){
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return getDataPath()+fileSeparator + "temp" + fileSeparator + relativePath;
    }

    public static String getDirPath(String countryCode, String itemDir, String dirType, String configKey, String dirName){
        String fileSeparator = FileSystems.getDefault().getSeparator();

        String dirPath = null;

        if(countryCode != null && !countryCode.isEmpty()) {
            dirPath = Config.getConfig(configKey + "." + countryCode);
            //대소문자 인식용
            if(dirPath == null){
                dirPath = Config.getConfig(configKey + "." + countryCode.toLowerCase());
            }
            if(dirPath == null){
                dirPath = Config.getConfig(configKey + "." + countryCode.toUpperCase());
            }
        }

        //국가별로 다르게 설정할 수 잇음
        //국내만 3천개의 종목이 넘고 미국은 만개의종목이 넘으므로 기본경로는 국가 코드가 들어가게 한다.
        if (dirPath == null) {

            if(countryCode == null){
                dirPath = TradingConfig.getTradingDataPath() + fileSeparator + itemDir + fileSeparator + dirType + fileSeparator +dirName;
            }else{
                countryCode = countryCode.toUpperCase();
                dirPath = TradingConfig.getTradingDataPath() + fileSeparator + itemDir + fileSeparator + countryCode + fileSeparator+ dirType + fileSeparator +dirName;
            }
        }

        return dirPath;
    }



    public static String getItemDir(String type){
        String compareType = type.toUpperCase();

        if(compareType.equals("STOCK")){
            return "stock";
        }else if(compareType.equals("COMMODITY")){
            return "commodities";
        }else if(compareType.equals("CURRENCY")){
            return "currencies";
        }else if(compareType.equals("INDEX")){
            return "indices";
        }else if(compareType.equals("BOND")){
            return "bonds";
        }else if(compareType.equals("CRYPTOCURRENCY")){
            return "cryptocurrency";
        }

        return type.toLowerCase();
    }


    public static String getFuturesCandlePath(Futures futures){

        String itemDir = getItemDir(futures.getUnderlyingAssetsType());

        String configKey = itemDir+".futures.candle.dir.path";
        String countryCode = CountryUtils.getCountryCode(futures.getFuturesId());


        return getDirPath(countryCode, itemDir,"futures", configKey, "candle");
    }

    public static String getFuturesCandleFilesPath(Futures futures, String interval){
        String fileSeparator = FileSystems.getDefault().getSeparator();
        return  getFuturesCandlePath(futures)+fileSeparator+futures.getFuturesId()+fileSeparator+interval;
    }


    public static void main(String[] args) {
        System.out.println(getTempPath());
    }

}
