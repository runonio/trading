package io.runon.trading.data.csv;

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
}
