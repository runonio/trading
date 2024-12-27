package io.runon.trading.data;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author macle
 */
@Data
public class NumbersData {
    String id;
    String name;

    BigDecimal [] numbers;

    public String toCsv(){
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",").append(name);

        for(BigDecimal number : numbers){
            sb.append(",").append(number);
        }
        return sb.toString();
    }

}
