package io.runon.trading.data.candle;

import com.seomse.commons.config.Config;
import com.seomse.commons.utils.FileUtil;
import io.runon.trading.CountryCode;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;
import io.runon.trading.data.file.CsvTimeLine;
import io.runon.trading.data.file.TimeFileOverride;
import io.runon.trading.data.file.TimeName;

import java.io.File;
import java.nio.file.FileSystems;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
/**
 * @author macle
 */
public class CandleDataUtils {

    @SuppressWarnings("ConstantValue")
    public static String getStockSpotCandlePath(){
        String nullCode = null;
        return getStockSpotCandlePath(nullCode);
    }

    public static String getStockSpotCandlePath(CountryCode countryCode){
        return getStockSpotCandlePath(countryCode.toString());
    }

    /**
     * 주식 캔들은 종목수가 많아서 국가별로 따로 관리할 수 있는 기능을 추가 한다. (용량분산)
     * 한국 종목만 3천개가 넘고, 미국까지 은 2만개가량의 종목 정보가 저장될 수 있다.
     * @param countryCode 국가코드 
     * @return 캔들 폴더 경로
     */
    public static String getStockSpotCandlePath(String countryCode){


        String fileSeparator = FileSystems.getDefault().getSeparator();

        String spotCandleDirPath = null;

        if(countryCode != null && !countryCode.isEmpty()) {
            spotCandleDirPath = Config.getConfig("stock.spot.candle.dir.path." + countryCode);
            //대소문자 인식용
            if(spotCandleDirPath == null){
                spotCandleDirPath = Config.getConfig("stock.spot.candle.dir.path." + countryCode.toLowerCase());
            }
            if(spotCandleDirPath == null){
                spotCandleDirPath = Config.getConfig("stock.spot.candle.dir.path." + countryCode.toUpperCase());
            }
        }

        //국가별로 다르게 설정할 수 잇음
        //국내만 3천개의 종목이 넘고 미국은 만개의종목이 넘으므로 기본경로는 국가 코드가 들어가게 한다.
        if (spotCandleDirPath == null) {

            if(countryCode == null){
                spotCandleDirPath = Config.getConfig("trading.data.path", TradingConfig.getTradingDataPath()) + fileSeparator + "stock" + fileSeparator +"spot" + fileSeparator +"candle";
            }else{
                countryCode = countryCode.toUpperCase();
                spotCandleDirPath = Config.getConfig("trading.data.path", TradingConfig.getTradingDataPath()) + fileSeparator + "stock" + fileSeparator  +countryCode + fileSeparator+"spot" + fileSeparator +"candle";
            }
        }

        return spotCandleDirPath;
    }


    public static String [] getCandlePaths(String dirPath){
        List<File> dirList = FileUtil.getDirList(dirPath);

        List<String> pathList = new ArrayList<>();

        for(File dir : dirList){
            boolean isParentsCandle = false;
            File parents = dir.getParentFile();
            if(parents == null){
                continue;
            }
            if(parents.getName().equals("candle")){
                isParentsCandle = true;
            }else{

                parents = parents.getParentFile();
                if(parents == null){
                    continue;
                }
                if(parents.getName().equals("candle")){
                    isParentsCandle = true;
                }
            }

            if(isParentsCandle && TradingTimes.isInterval(dir.getName())){
                pathList.add(dir.getAbsolutePath());
            }

        }

        String [] paths = pathList.toArray(new String[0]);
        pathList.clear();

        return paths;
    }


    public static void changeTimeZone(String dirPath, ZoneId zoneId){
        String [] candlePaths = getCandlePaths(dirPath);
        for(String candlePath : candlePaths){
            long intervalTime = TradingTimes.getIntervalTime(new File(candlePath).getName());
            TimeFileOverride timeFileOverride =  new TimeFileOverride(candlePath, new CsvTimeLine(), TimeName.getCandleType(intervalTime));
            timeFileOverride.setZoneId(zoneId);
            timeFileOverride.run();
        }
    }

}