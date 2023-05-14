package io.runon.trading.data.file;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.validation.NumberNameFileValidation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 시간정보가 있는 파일
 * @author macle
 */
public class TimeFiles {

    public static File [] getFilesDir(String path){
        File dirFile = new File(path);
        if(!dirFile.isDirectory()){
            return new File[0];
        }


        File [] files = dirFile.listFiles();
        if(files == null || files.length == 0){
            return new File[0];
        }

        NumberNameFileValidation validation = new NumberNameFileValidation();


        List<File> fileList = new ArrayList<>();
        for(File file : files){
            if(validation.isValid(file)){
                fileList.add(file);
            }
        }

        files = fileList.toArray(new File[0]);

        Arrays.sort(files, FileUtil.SORT_NAME_LONG);

        return files;
    }

    public static File [] getFilesDirs(String path){
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

        //noinspection RedundantLengthCheck
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
    public static void main(String[] args) {
        moveDir("D:\\data\\cryptocurrency\\merge\\volume", "D:\\data\\cryptocurrency\\merge\\volume\\temp_time_override");
    }


}
