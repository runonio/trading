package io.runon.trading.data.csv;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.validation.NumberNameFileValidation;

import java.io.File;
import java.math.BigDecimal;

/**
 * csv 공통
 * @author macle
 */
public class CsvCommon {

    public static void append(StringBuilder sb, BigDecimal bigDecimal){
        sb.append(",");
        if(bigDecimal != null){
            sb.append(bigDecimal.stripTrailingZeros().toPlainString());
        }
    }

    public static BigDecimal getBigDecimal(String value){
        if(value == null){
            return null;
        }
        value = value.trim();
        if(value.length() == 0){
            return null;
        }

        return new BigDecimal(value);
    }


    public static long getLastOpenTime(String path){

        File[] files = FileUtil.getFiles(path, new NumberNameFileValidation(), FileUtil.SORT_NAME_LONG);

        if(files.length == 0){
            return -1;
        }
        File lastFile = files[files.length-1];
        String lastLine = FileUtil.getLastTextLine(lastFile);
        int index = lastLine.indexOf(',');
        return Long.parseLong(lastLine.substring(0, index));
    }
}
