package io.runon.trading.data.file;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.string.Check;
import com.seomse.commons.validation.NumberNameFileValidation;
import io.runon.trading.TradingConfig;
import io.runon.trading.TradingTimes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 시간정보가 있는 파일
 * @author macle
 */
public class TimeFiles {

    public static File [] getFilesDir(String path){
        return FileUtil.getInFiles(path, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);
    }

    public static boolean isInDir(String path){
        File dirFile = new File(path);
        if(!dirFile.isDirectory()){
            return false;
        }

        File [] files = dirFile.listFiles();
        if(files == null || files.length == 0){
            return false;
        }

        NumberNameFileValidation validation = new NumberNameFileValidation();

        for(File file : files){
            if(validation.isValid(file)){
                return true;
            }
        }
        return false;
    }

    public static boolean isInDirs(String path){
        File [] files = FileUtil.getFiles(path, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);
        return files.length > 0;
    }

    public static String getLastLine(String dirPath){
        File[] files = FileUtil.getFiles(dirPath, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG_DESC);

        //noinspection RedundantLengthCheck,ConstantValue
        if(files == null || files.length == 0){
            return null;
        }

        for(File file : files){
            String line = FileUtil.getLastTextLine(file);
            if("".equals(line)){
                continue;
            }
            return line;
        }

        return null;
    }

    public static void moveDir(String dirPath, String moveDirPath){
        File[] files = FileUtil.getInFiles(dirPath, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);

        if(files.length == 0){
            return ;
        }

        File moveDirFie = new File(moveDirPath);
        //noinspection ResultOfMethodCallIgnored
        moveDirFie.mkdirs();

        for(File file : files){
            FileUtil.move(file.getAbsolutePath(), moveDirPath +"/" + file.getName(), false);
        }

    }

    public static String [] getApiCandleDataIntervalDirs(){

        List<String> dirList = new ArrayList<>();

        String [] apiDataPaths = TradingConfig.getApiCandleDataPaths();

        String dataPath = TradingConfig.getTradingDataPath();

        dataPath = dataPath.replace("\\","/");

        for(String apiDataPath : apiDataPaths){
            apiDataPath = apiDataPath.replace("\\","/");
            if(!apiDataPath.startsWith(dataPath)){
                apiDataPath = dataPath +"/" + apiDataPath;
            }

            List<String> intervalDirList = getIntervalDirList(apiDataPath);
            for(String intervalDir : intervalDirList){
                if(intervalDir.contains("/candle/")){
                    dirList.add(intervalDir);
                }
            }

            dirList.addAll(getIntervalDirList(apiDataPath));
        }

        String [] array = dirList.toArray(new String[0]);
        dirList.clear();

        return array;
    }

    public static String [] getIntervalDirs(String dirPath){
        return getIntervalDirs(new File(dirPath));
    }

    public static String [] getIntervalDirs(File dirFile){
        List<String> list = getIntervalDirList(dirFile);
        return list.toArray(new String[0]);
    }

    public static List<String> getIntervalDirList(String dirPath) {
        return getIntervalDirList(new File(dirPath));
    }
    public static List<String> getIntervalDirList(File dirFile){
        if(!dirFile.isDirectory()){
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>();
        addIntervalDirs(list, dirFile);
        return list;
    }

    public static void addIntervalDirs(List<String> list, File dirFile){
        if(!dirFile.isDirectory()){
            return;
        }

        if (isIntervalDir(dirFile.getName())){
           list.add(getDataRelativePath(dirFile.getAbsolutePath()));
        }

        File [] array = dirFile.listFiles();
        if(array == null || array.length == 0){
            return;
        }

        for(File f : array){
            if(f.isDirectory()){
                addIntervalDirs(list, f);
            }
        }
    }

    public static String getDataRelativePath(String path){
        String dataPath = TradingConfig.getTradingDataPath();
        dataPath = dataPath.replace("\\","/");

        path = path.replace("\\","/");

        if(path.equals(dataPath)){
            return "";
        }

        if(path.startsWith(dataPath)){
            return path.substring(dataPath.length()+1);
        }

        return path;
    }

    public static boolean isIntervalDir(String dirName){
        if(dirName == null || dirName.length() == 0){
            return false;
        }

        char last = dirName.charAt(dirName.length()-1);
        if(last == 'w' || last == 'd' ||  last == 'h' || last == 'm' ||  last == 's'  ){

            String numberText = dirName.substring(0, dirName.length()-1);
            return Check.isNumber(numberText);
        }

        return false;
    }

    public static String getPathInterval(String dirPath){
        String intervalGet = dirPath.replace("\\","/");
        int lastIndex = intervalGet.lastIndexOf("/");
        return intervalGet.substring(lastIndex+1);
    }

    public static long getPathIntervalTime(String dirPath){
        String interval = getPathInterval(dirPath);
        return TradingTimes.getIntervalTime(interval);
    }

    public static TimeName.Type getPathTimeNameType(String dirPath){
        long intervalTime = getPathIntervalTime(dirPath);
        return TimeName.getDefaultType(intervalTime);
    }


    public static void main(String[] args) {

        String [] dirs = getApiCandleDataIntervalDirs();
        for(String d : dirs){
            System.out.println(d);
        }

//        moveDir("D:\\data\\cryptocurrency\\merge\\volume", "D:\\data\\cryptocurrency\\merge\\volume\\temp_time_override");
    }


}
