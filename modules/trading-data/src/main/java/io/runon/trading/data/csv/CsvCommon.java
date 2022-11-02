package io.runon.trading.data.csv;

import io.runon.trading.data.file.TimeFiles;

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

        String lastLine = TimeFiles.getLastLine(path);

        if(lastLine == null){
            return -1;
        }
        int index = lastLine.indexOf(',');
        return Long.parseLong(lastLine.substring(0, index));

    }
}
