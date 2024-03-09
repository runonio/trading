package io.runon.trading.data.file;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.validation.NumberNameFileValidation;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * timeLine 생성기
 * line을 분석하여 line에 맞는 TimeLime 클래스를 돌려준다.
 * @author macle
 */
@SuppressWarnings({"ConstantValue", "StringEqualsEmptyString"})
public class TimeLineFactory {


    public static TimeLine newTimeLine(File file) {
        if(file.isDirectory()){
            File [] files = FileUtil.getFiles(file, new NumberNameFileValidation());
            return newTimeLine(files);
        }else{
            String line = FileUtil.getLine(file, StandardCharsets.UTF_8, 0);
            return newTimeLine(line);
        }

    }
    public static TimeLine newTimeLine(File [] dataFiles ) {
        String line = null;
        for(File dataFile : dataFiles){
            try {
                line  = FileUtil.getLine(dataFile, StandardCharsets.UTF_8, 0);
                if(line == null || "".equals(line)){
                    continue;
                }
                break;
            }catch (Exception ignore){}
        }

        return newTimeLine(line);
    }

    public static TimeLine newTimeLine(String line){

        line = line.trim();
        if(line.startsWith("{")){
            return new JsonTimeLine();
        }else{
            return new CsvTimeLine();
        }

    }

}
