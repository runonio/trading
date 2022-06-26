package io.runon.trading.data.file;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.string.Check;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 시간정보가 있는 파일
 * @author macle
 */
public class TimeFiles {


    public static File[] getTimeFiles(String path, boolean isAsc){
        return getTimeFiles(new File(path), isAsc);
    }

    public static File[] getTimeFiles(File file, boolean isAsc){

        File [] array = FileUtil.getFiles(file.getAbsolutePath());

        if(array == null || array.length == 0){
            return Files.EMPTY_FILES;
        }


        List<File> list = new ArrayList<>();
        for(File f : array){
            if(!f.isFile()){
                continue;
            }

            if(Check.isNumber(f.getName())){
                list.add(f);
            }
        }

        if(list.size() == 0){
            return Files.EMPTY_FILES;
        }

        File [] files = list.toArray(new File[0]);
        if(isAsc) {
            Arrays.sort(files, FileUtil.SORT_NAME_LONG);
        }else{
            Arrays.sort(files, FileUtil.SORT_NAME_LONG_DESC);
        }


        return files;
    }

}
