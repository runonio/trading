package io.runon.trading.closed.days;

import com.seomse.commons.utils.FileUtil;
import io.runon.trading.data.file.YmdFiles;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author macle
 */
public class ClosedDaysSet implements ClosedDays{
    private final Set<String> ymdSet;

    public ClosedDaysSet(Set<String> ymdSet){
        this.ymdSet = ymdSet;
    }

    public ClosedDaysSet(){
        ymdSet = new HashSet<>();
    }

    public boolean addYmd(String ymd){
        return ymdSet.add(ymd);
    }

    public boolean removeYmd(String ymd){
        return ymdSet.remove(ymd);
    }

    @Override
    public boolean isClosedDay(String ymd) {
        return ymdSet.contains(ymd);
    }

    public void clear(){
        ymdSet.clear();
    }


    public void loadFile(String filePath){
        String text = FileUtil.getFileContents(new File(filePath), StandardCharsets.UTF_8);
        String [] ymds = text.split("\n");
        for(String ymd : ymds){
            ymd = ymd.trim();
            if(ymd.equals("")){
                continue;
            }
            ymdSet.add(ymd);
        }
    }

    public void outFile(String filePath){
        String [] arrays = ymdSet.toArray(new String[0]);
        Arrays.sort(arrays);
        YmdFiles.outAppend(filePath, arrays);
    }

    public static void main(String[] args) {
        String []  ymds = {"20220101","20210101","20010501", "19500512","20240101"};
        Arrays.sort(ymds);

        for (String ymd : ymds){
            System.out.println(ymd);
        }

    }


}
