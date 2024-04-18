package io.runon.trading.data.file;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.validation.NumberNameFileValidation;
import io.runon.trading.data.DataPathTimeRange;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일관련 정보 활용
 * @author macle
 */
@SuppressWarnings("RedundantLengthCheck")
public class Files {
    public static final File [] EMPTY_FILES = new File[0];

    public static File [] getLastChildDirs(File dirFile){
        List<File> list = new ArrayList<>();
        addListChildDirs(dirFile.listFiles(), list);
        return list.toArray(new File[0]);
    }

    public static void addListChildDirs(File [] files, List<File> list){
        if(files == null || files.length ==0){
            return;
        }

        for(File file : files){
            if(!file.isDirectory()){
                continue;
            }
            if(isNextDir(file.listFiles())){
                addListChildDirs(file.listFiles(), list);
            }else{
                list.add(file);
            }
        }
    }

    public static boolean isNextDir(File [] files){
        if(files == null || files.length ==0){
            return false;
        }

        for(File file: files){
            if(file.isDirectory()){
                return true;
            }
        }
        return false;
    }

    public static File [] getTimeFiles(File dirFile){
        return  FileUtil.getFiles(dirFile, new NumberNameFileValidation(),  FileUtil.SORT_NAME_LONG);
    }

    public static DataPathTimeRange getTimeRange(File dirFile){
        File [] files = getTimeFiles(dirFile);
        if(files.length ==0){
            return null;
        }

        TimeLine timeLine = TimeLineFactory.newTimeLine(files);

        String startLine = FileUtil.getLine(files[0], StandardCharsets.UTF_8, 0);
        String endLine = FileUtil.getLastTextLine(files[files.length-1]);

        return new DataPathTimeRange(dirFile.getAbsolutePath(), timeLine.getTime(startLine), timeLine.getTime(endLine));
    }


    public static void main(String[] args) {
        File [] files = getLastChildDirs(new File("D:\\data"));

        for(File file : files){
            System.out.println(file.getAbsoluteFile());
        }

        System.out.println(files.length);
    }

}
